package pl.kodstark.rxjava_sandbox;

import io.vertx.core.Vertx;
import jakarta.annotation.PreDestroy;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class VertxServerRunner implements CommandLineRunner {

  @Value("${vertx.listen-port:8081}")
  private int listenPort;

  private Vertx vertx;

  @Override
  public void run(String... args) {
    vertx = Vertx.vertx();
    CountDownLatch latch = new CountDownLatch(1);
    vertx.deployVerticle(new VertxServer(listenPort), ar -> latch.countDown());
    try {
      latch.await(30_000, TimeUnit.MILLISECONDS);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  @PreDestroy
  public void stop() {
    if (vertx != null) {
      CountDownLatch latch = new CountDownLatch(1);
      vertx.close(ar -> latch.countDown());
      try {
        latch.await(30_000, TimeUnit.MILLISECONDS);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
