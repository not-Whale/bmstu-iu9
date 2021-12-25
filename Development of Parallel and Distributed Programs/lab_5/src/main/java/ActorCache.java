import akka.actor.AbstractActor;

import java.util.HashMap;
import java.util.Map;

public class ActorCache extends AbstractActor {
    private final Map<String, Long> results = new HashMap<>();


    @Override
    public Receive createReceive() {
        return null;
    }
}
