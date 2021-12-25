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
import akka.stream.javadsl.Keep;
import akka.stream.javadsl.Sink;

import akka.stream.javadsl.Source;
import org.asynchttpclient.Dsl;
import org.asynchttpclient.Request;
import org.asynchttpclient.Response;
import scala.Int;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
        return Flow.of(HttpRequest.class)
                .map(req -> {
                    Query query = req.getUri().query();
                    String url = query.get(QUERY_PARAMETER_URL).get();
                    int count = Integer.parseInt(query.get(QUERY_PARAMETER_COUNT).get());
                    return new Pair<>(url, count);
                })
                .mapAsync(1, req ->
                    Patterns.ask(
                            actorSystem,
                            new MessageGetResult(req.first()),
                            java.time.Duration.ofMillis(5000))
                            .thenCompose(res -> {
                                if (((Optional<Long>) res).isPresent()) {
                                    return CompletableFuture.completedFuture(new Pair<>(req.first(), ((Optional<Long>) res).get()));
                                } else {
                                    Sink<Integer, CompletionStage<Long>> fold = Sink.fold(0L, (Function2<Long, Integer, Long>) Long::sum);
                                    Sink<Pair<String, Integer>, CompletionStage<Long>> sink = Flow
                                            .<Pair<String, Integer>>create()
                                            .mapConcat(r -> new ArrayList<>(Collections.nCopies(r.second(), r.first())))
                                            .mapAsync(req.second(), url -> {
                                                long start = System.currentTimeMillis();
                                                Request request = Dsl.get(url).build();
                                                CompletableFuture<Response> whenResponse = Dsl.asyncHttpClient().executeRequest(request).toCompletableFuture();
                                                return whenResponse.thenCompose(response -> {
                                                    int duration = (int) (System.currentTimeMillis() - start);
                                                    return CompletableFuture.completedFuture(duration);
                                                });
                                            })
                                            .toMat(fold, Keep.right());
                                    return Source.from(Collections.singletonList(req))
                                            .toMat(sink, Keep.right())
                                            .run(actorMaterializer)
                                            .thenApply(sum -> new Pair<>(req.first(), sum / req.second()));
                                }
                            }))
                .map(res -> {
                    actorSystem.tell(
                            new MessageCacheResult(res.firts(), res.second()),
                            ActorRef.noSender()
                    );
                    return HttpResponse.create().withEntity(res.firts() + ": " + res.second().toString());
                });
    }

    static class MessageGetResult {
        private final String url;

        public MessageGetResult(String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }
    }

    static class MessageCacheResult {
        
    }
}
