import akka.actor.AbstractActor;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class ActorTester extends AbstractActor {
    private static final String SCRIPT_ENGINE_NAME = "nashorn";
    private static final String PASSED_STATUS = "PASSED";
    private static final String FAILED_STATUS = "FAILED";
    private static final String CRASHED_STATUS = "CRASHED";
    private static final String EMPTY_STRING = "";

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(
                ActorRouter.MessageTest.class,
                message -> sender().tell(
                        runTest(message),
                        self()
                )
        ).build();
    }

    private MessageStoreTestResult runTest(ActorRouter.MessageTest message) {
        String status;
        String expected = message.getTest().getExpectedResult();
        String received;

        try {
            received = runJS(
                    message.getJsScript(),
                    message.getFunctionName(),
                    message.getTest().getParameters()
            );
            status = isEqual(expected, received) ? PASSED_STATUS : FAILED_STATUS;
        } catch (ScriptException | NoSuchMethodException e) {
            status = CRASHED_STATUS;
            received = EMPTY_STRING;
        }
        return new MessageStoreTestResult(
                message.getPackageID(),
                status,
                message.getTest().getTestName(),
                expected,
                received
        );
    }

    private String runJS(String jsScript, String functionName, Object[] parameters) throws ScriptException, NoSuchMethodException {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName(SCRIPT_ENGINE_NAME);
        engine.eval(jsScript);
        Invocable invocable = (Invocable) engine;
        return invocable.invokeFunction(functionName, parameters).toString();
    }

    private static boolean isEqual(String expected, String received) {
        return expected.equals(received);
    }

    static class MessageStoreTestResult {
        private final String packageID;
        private final TestResult result;

        public MessageStoreTestResult(
                String packageID,
                String status,
                String testName,
                String expectedResult,
                String receivedResult) {
            this.packageID = packageID;
            this.result = new TestResult(status, testName, expectedResult, receivedResult);
        }

        public String getPackageID() {
            return packageID;
        }

        public TestResult getTestResult() {
            return result;
        }

        @Override
        public String toString() {
            return "Package ID: "
                    + getPackageID()
                    + "\nResult: "
                    + getTestResult();
        }
    }
}
