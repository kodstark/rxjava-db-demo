package pl.kodstark.rxjava_sandbox;

import io.reactivex.rxjava3.core.Flowable;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.impl.NoStackTraceThrowable;
import io.vertx.core.parsetools.RecordParser;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import io.vertx.rxjava3.SingleHelper;
import io.vertx.rxjava3.core.Vertx;
import io.vertx.rxjava3.core.http.HttpClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@Slf4j
@ExtendWith(VertxExtension.class)
class FileStreamerRxHttpClientTests {

  private VertxServer sut;
  private HttpClient client;

  @BeforeEach
  void deploy_verticle(Vertx vertx, VertxTestContext ctx) {
    sut = new VertxServer(0);
    vertx
        .deployVerticle(sut)
        .subscribe(SingleHelper.toObserver(ctx.succeeding(id -> ctx.completeNow())));
    client = vertx.createHttpClient();
  }

  @Test
  void load_file(VertxTestContext ctx) {
    RecordParser parser = RecordParser.newDelimited("\n", h -> log.info("r={}", h.toString()));
    client
        .rxRequest(HttpMethod.GET, sut.actualPort(), "localhost", "/stream?file=stream2.txt")
        .flatMapPublisher(
            request ->
                request
                    .rxSend()
                    .flatMapPublisher(
                        response -> {
                          if (response.statusCode() == 200) {
                            return response.toFlowable();
                          } else {
                            return Flowable.error(new NoStackTraceThrowable("Invalid response"));
                          }
                        }))
        .subscribe(
            body -> parser.handle(body.getDelegate()),
            ctx::failNow,
            ctx::completeNow);
  }
}
