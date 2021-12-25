import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.Route;
import akka.pattern.Patterns;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import scala.concurrent.Future;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletionStage;

public class JSTester extends AllDirectives {
    private static final String ACTOR_SYSTEM_NAME = "js_tester";

    public static void main(String[] args) throws IOException {
        ActorSystem actorSystem = ActorSystem.create(ACTOR_SYSTEM_NAME);
        ActorRef actorRouter = actorSystem.actorOf(Props.create(ActorRouter.class, actorSystem));

        final Http http = Http.get(actorSystem);
        final ActorMaterializer actorMaterializer = ActorMaterializer.create(actorSystem);

        JSTester instance = new JSTester();
        final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow =
                instance.createRoute(actorRouter).flow(actorSystem, actorMaterializer);
        final CompletionStage<ServerBinding> binding = http.bindAndHandle(
                routeFlow,
                ConnectHttp.toHost("localhost", 8080),
                actorMaterializer
        );
        System.out.println("Server online at http://localhost:8080/\nPress RETURN to stop...");
        System.in.read();
        binding
                .thenCompose(ServerBinding::unbind)
                .thenAccept(unbound -> actorSystem.terminate());
    }

    private Route createRoute(ActorRef actorRouter) {
        return route(
                path("test", () ->
                        route (
                                post(() ->
                                        entity(Jackson.unmarshaller(MessageTestsPackage.class), message -> {
                                            actorRouter.tell(message, ActorRef.noSender());
                                            return complete("Test started!");
                                        }))
                        )),
                path("result", () ->
                        route(
                                get(() ->
                                        parameter("packageId", (id) -> {
                                            Future<Object> result = Patterns.ask(
                                                    actorRouter,
                                                    new MessageGetTestPackageResult(id),
                                                    5000
                                            );
                                            return completeOKWithFuture(result, Jackson.marshaller());
                                        }))
                        ))
        );
    }

    static class MessageGetTestPackageResult {
        private final String packageID;

        public MessageGetTestPackageResult(String packageID) {
            this.packageID = packageID;
        }

        public String getPackageID() {
            return packageID;
        }
    }

    static class MessageTestsPackage {
        private final String packageID;
        private final String jsScript;
        private final String functionName;
        private final List<TestBody> tests;

        @JsonCreator
        public MessageTestsPackage(
                @JsonProperty("packageId") String packageID,
                @JsonProperty("jsScript") String jsScript,
                @JsonProperty("functionName") String functionName,
                @JsonProperty("tests") List<TestBody> tests) {
            this.packageID = packageID;
            this.jsScript = jsScript;
            this.functionName = functionName;
            this.tests = tests;
        }

        public List<TestBody> getTests() {
            return tests;
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
    }
}
