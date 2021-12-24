import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.routing.ActorRefRoutee;
import akka.routing.RoundRobinRoutingLogic;
import akka.routing.Routee;
import akka.routing.Router;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class ActorRouter extends AbstractActor {
    private static final int TESTERS_AMOUNT = 5;
    private final ActorRef storager;
    private final Router router;

    {
        storager = getContext().actorOf(Props.create(ActorStorager.class));
        List<Routee> routeeList = new ArrayList<>();
        for (int i = 0; i < TESTERS_AMOUNT; i++) {
            ActorRef actorTester = getContext().actorOf(Props.create(ActorTester.class));
            getContext().watch(actorTester);
            routeeList.add(new ActorRefRoutee(actorTester));
        }
        router = new Router(new RoundRobinRoutingLogic(), routeeList);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(
                JSTester.MessageTestsPackage.class,
                message -> {
                    String packageID = message.getPackageID();
                    String jsScript = message.getJsScript();
                    String functionName = message.getFunctionName();

                    for (TestBody test : message.getTests()) {
                        router.route(new MessageTest(packageID, jsScript, functionName, test), storager);
                    }
                }
        ).match(
                JSTester.MessageGetTestPackageResult.class,
                message -> storager.tell(message, sender())
        ).build();
    }

    static class MessageTest {
        private final String packageID;
        private final String jsScript;
        private final String functionName;
        private final TestBody test;

        @JsonCreator
        public MessageTest(
                @JsonProperty("packageID") String packageID,
                @JsonProperty("jsScript") String jsScript,
                @JsonProperty("functionName") String functionName,
                @JsonProperty("test") TestBody test) {
            this.packageID = packageID;
            this.jsScript = jsScript;
            this.functionName = functionName;
            this.test = test;
        }

        public String getPackageID() {
            return packageID;
        }

        public String getJsScript() {
            return jsScript;
        }

        public String getFunctionName() {
            return functionName;
        }

        public TestBody getTest() {
            return test;
        }
    }
}
