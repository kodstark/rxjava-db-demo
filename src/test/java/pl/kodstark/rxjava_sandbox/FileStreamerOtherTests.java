package pl.kodstark.rxjava_sandbox;

import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import io.vertx.rxjava3.SingleHelper;
import io.vertx.rxjava3.core.Vertx;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(VertxExtension.class)
class FileStreamerOtherTests {

  @BeforeEach
  void deploy_verticle(Vertx vertx, VertxTestContext ctx) {
    vertx
        .deployVerticle(new VertxServer(0))
        .subscribe(SingleHelper.toObserver(ctx.succeeding(id -> ctx.completeNow())));
  }

  @Test
  void load_file(VertxTestContext ctx) {
    ctx.completeNow();
  }
}
