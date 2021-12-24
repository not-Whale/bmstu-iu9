import akka.actor.AbstractActor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActorStorager extends AbstractActor {
    private final Map<String, List<TestResult>> results = new HashMap<>();

    @Override
    public Receive createReceive() {
        return null;
    }
}
