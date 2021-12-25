import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.model.Query;
import akka.japi.Pair;
import akka.japi.function.Function2;
import akka.pattern.Patterns;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.Sink;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class ResponseTimeAnalyser {
    private static final String ACTOR_SYSTEM_NAME = "response time analyser";
    private static final String QUERY_PARAMETER_URL = "testUrl";
    private static final String QUERY_PARAMETER_COUNT = "count";

    public static void main(String[] args) throws IOException {
        System.out.println("start!");
        ActorSystem actorSystem = ActorSystem.create(ACTOR_SYSTEM_NAME);

        final Http http = Http.get(actorSystem);
        final ActorMaterializer actorMaterializer = ActorMaterializer.create(actorSystem);

        final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow = flowHttpRequest(actorMaterializer, actorSystem);
        final CompletionStage<ServerBinding> binding = http.bindAndHandle(
                routeFlow,
                ConnectHttp.toHost("localhost", 8080),
                actorMaterializer
        );
        System.out.println("Server online at http://localhost:8080/\nPress RETURN to stop...");
        System.in.read();
        binding
                .thenCompose(ServerBinding::unbind)
                .thenAccept(unbound -> system.terminate()); // and shutdown when done
    }

    private static Flow<HttpRequest, HttpResponse, NotUsed> flowHttpRequest(ActorMaterializer actorMaterializer, ActorRef actorSystem) {
        return Flow.of(HttpRequest.class).map(
                request -> {
                    Query query = request.getUri().query();
                    String url = query.get(QUERY_PARAMETER_URL).get();
                    int count = Integer.parseInt(query.get(QUERY_PARAMETER_COUNT).get());
                    return new Pair<>(url, count);
                }).mapAsync(
                        1, request -> {
                    Patterns.ask(
                            actorSystem,
                            new MessageGetResult(request.first()),
                            java.time.Duration.ofMillis(5000)
                    ).thenCompose(
                            result -> {
                                if (((Optional<Long>)result).isPresent()) {
                                    return CompletableFuture.completedFuture(new Pair<>(request.first(), ((Optional<Long>) result).get()));
                                } else {
                                    Sink<Integer, CompletionStage<Long>> fold = Sink.fold(0L, (Function2<Long, Integer, Long>) Long::)
                                }
                            }

                    )
                }
        )
    }
}
