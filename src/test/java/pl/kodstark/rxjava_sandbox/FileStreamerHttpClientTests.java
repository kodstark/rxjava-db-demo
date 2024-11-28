package pl.kodstark.rxjava_sandbox;

import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.parsetools.RecordParser;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import io.vertx.rxjava3.SingleHelper;
import io.vertx.rxjava3.core.Vertx;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@Slf4j
@ExtendWith(VertxExtension.class)
class FileStreamerHttpClientTests {

  private VertxServer sut;
  private HttpClient client;

  @BeforeEach
  void deploy_verticle(Vertx vertx, VertxTestContext ctx) {
    sut = new VertxServer(0);
    vertx
        .deployVerticle(sut)
        .subscribe(SingleHelper.toObserver(ctx.succeeding(id -> ctx.completeNow())));
    client = vertx.getDelegate().createHttpClient();
  }

  @Test
  void load_file(VertxTestContext ctx) {
    RecordParser parser = RecordParser.newDelimited("\n", h -> log.info("r={}", h.toString()));
    client
        .request(HttpMethod.GET, sut.actualPort(), "localhost", "/stream?file=stream1.txt")
        .compose(HttpClientRequest::send)
        .onComplete(
            ar -> {
              if (ar.succeeded()) {
                HttpClientResponse response = ar.result();
                response.handler(parser);
                response.endHandler(e -> ctx.completeNow());
              } else {
                ctx.failNow(ar.cause());
              }
            });
  }
}
