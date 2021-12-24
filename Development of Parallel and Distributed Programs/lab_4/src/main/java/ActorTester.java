import akka.actor.AbstractActor;

public class ActorTester extends AbstractActor {
    private static final String PASSED_STATUS = "PASSED";
    private static final String FAILED_STATUS = "FAILED";
    private static final String CRASHED_STATUS = "CRASHED";

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
            received = execute
        }
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
