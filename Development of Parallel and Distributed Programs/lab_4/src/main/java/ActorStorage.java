import akka.actor.AbstractActor;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.*;

public class ActorStorage extends AbstractActor {
    private final Map<String, List<TestResult>> results = new HashMap<>();

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(
                ActorTester.MessageStoreTestResult.class,
                this::storeResult
        ).match(
                JSTester.MessageGetTestPackageResult.class,
                request -> sender().tell(
                        new MessageReturnResults(
                                request.getPackageID(),
                                results.get(request.getPackageID())
                        ),
                        self()
                )
        ).build();
    }

    private void storeResult(ActorTester.MessageStoreTestResult message) {
        String packageID = message.getPackageID();
        if (results.containsKey(packageID)) {
            results.get(packageID).add(message.getTestResult());
        } else {
            results.put(
                    message.getPackageID(),
                    new ArrayList<>(
                            Collections.singleton(message.getTestResult())
                    )
            );
        }
        System.out.println("Received message: " + message);
    }

    static class MessageReturnResults {
        private final String packageID;
        private final List<TestResult> results;

        @JsonCreator
        public MessageReturnResults(
                @JsonProperty("packageID") String packageID,
                @JsonProperty("results") List<TestResult> results) {
            this.packageID = packageID;
            this.results = results;
        }

        public String getPackageID() {
            return packageID;
        }

        public List<TestResult> getResults() {
            return results;
        }
    }
}
