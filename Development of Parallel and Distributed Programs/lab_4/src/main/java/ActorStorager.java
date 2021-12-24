import akka.actor.AbstractActor;

import java.util.HashMap;

public class ActorStorager extends AbstractActor {
    private final Map<String, List<TestResult>> results = new HashMap<>();

    @Override
    public Receive createReceive() {
        return null;
    }
}
