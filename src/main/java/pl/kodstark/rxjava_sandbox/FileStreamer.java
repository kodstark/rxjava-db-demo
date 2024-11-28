package pl.kodstark.rxjava_sandbox;

import io.vertx.core.file.FileSystem;
import io.vertx.core.file.OpenOptions;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileStreamer {

  private final RoutingContext context;

  public FileStreamer(RoutingContext context) {
    this.context = context;
  }

  public void streamFile() {
    String filePath = context.request().getParam("file");
    if (filePath == null || filePath.isEmpty()) {
      context.response().setStatusCode(400).end("File path is required in the URL.");
      return;
    }
    FileSystem fs = context.vertx().fileSystem();
    OpenOptions opts = new OpenOptions().setRead(true).setCreate(false);
    fs.open(
        filePath,
        opts,
        result -> {
          if (result.succeeded()) {
            var file = result.result();
            HttpServerResponse response = context.response();
            log.info("Streaming a file {}", filePath);
            response.setStatusCode(200).putHeader("Content-Type", "text/plain").setChunked(true);
            file.pipeTo(response);
          } else {
            context
                .response()
                .setStatusCode(500)
                .end("Error reading file: " + result.cause().getMessage());
          }
        });
  }
}
