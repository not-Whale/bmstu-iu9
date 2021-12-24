import akka.actor.AbstractActor;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActorStorager extends AbstractActor {
    private final Map<String, List<TestResult>> results = new HashMap<>();

    @Override
    public Receive createReceive() {
        return null;
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
