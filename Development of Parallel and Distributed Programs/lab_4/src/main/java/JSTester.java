import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.http.javadsl.Http;
import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.Route;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;

import java.io.IOException;

public class JSTester extends AllDirectives {
    private static final String ACTOR_SYSTEM_NAME = "js_tester";

    public static void main(String[] args) throws IOException {
        ActorSystem actorSystem = ActorSystem.create(ACTOR_SYSTEM_NAME);
        ActorRef actorRouter = actorSystem.actorOf(Props.create(ActorRouter.class));

        final Http http = Http.get(actorSystem);
        final ActorMaterializer actorMaterializer = ActorMaterializer.create(actorSystem);

        JSTester instance = new JSTester();
        final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow =
                instance.createRoute(actorRouter).flow(actorSystem, actorMaterializer);
    }

    private Route createRoute(ActorRef actorRouter) {
        return route (
                path("test", () ->
                        route (
                                post(() ->
                                        entity(Jackson.unmarshaller())
                        )
                        )
        )
    }
}
