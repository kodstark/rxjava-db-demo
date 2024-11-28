package pl.kodstark.rxjava_sandbox;

import io.vertx.core.parsetools.RecordParser;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.codec.BodyCodec;
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
class FileStreamerBodyCodecTests {

  private VertxServer sut;
  private WebClient client;

  @BeforeEach
  void deploy_verticle(Vertx vertx, VertxTestContext ctx) {
    sut = new VertxServer(0);
    vertx
        .deployVerticle(sut)
        .subscribe(SingleHelper.toObserver(ctx.succeeding(id -> ctx.completeNow())));
    client = WebClient.create(vertx.getDelegate());
  }

  @Test
  void load_file(VertxTestContext ctx) {
    RecordParser parser = RecordParser.newDelimited("\n", b -> log.info("r={}", b.toString()));
    RecordParserWriteStream bridge = new RecordParserWriteStream(parser);
    client
        .get(sut.actualPort(), "localhost", "/stream?file=stream2.txt")
        .as(BodyCodec.pipe(bridge))
        .send(
            ar -> {
              if (ar.succeeded()) {
                ctx.completeNow();
              } else {
                ctx.failNow(ar.cause());
              }
            });
  }
}
