package pl.kodstark.rxjava_sandbox;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class VertxServer implements CommandLineRunner {

  private static final Logger LOG = LoggerFactory.getLogger(VertxServer.class);

  @Override
  public void run(String... args) {
    Vertx vertx = Vertx.vertx();
    HttpServer server = vertx.createHttpServer();
    Router router = Router.router(vertx);
    router.route("/hello").handler(ctx -> ctx.response().end("Hello from Vert.x!"));
    router.route("/stream").handler(ctx -> new FileStreamer(ctx).streamFile());
    server.requestHandler(router);
    server.listen(
        8081,
        ar -> {
          if (ar.succeeded()) {
            LOG.info("Vert.x HTTP server started on port 8081");
          } else {
            throw new RuntimeException("Failed to start Vert.x server", ar.cause());
          }
        });
  }
}
