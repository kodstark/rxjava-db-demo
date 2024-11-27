package pl.kodstark.rxjava_sandbox;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(
    properties = {
      "vertx.listen-port=0",
    })
class RxjavaSandboxApplicationTests {

  @Test
  void contextLoads() {}
}
