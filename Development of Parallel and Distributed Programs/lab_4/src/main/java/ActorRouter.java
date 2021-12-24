import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.routing.Router;

public class ActorRouter extends AbstractActor {
    private static final int TESTERS_AMOUNT = 10;

    private final ActorRef storager;
    private final Router router;


    @Override
    public Receive createReceive() {
        return null;
    }
}
