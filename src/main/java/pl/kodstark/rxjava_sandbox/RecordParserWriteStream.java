package pl.kodstark.rxjava_sandbox;

import io.vertx.codegen.annotations.Nullable;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.parsetools.RecordParser;
import io.vertx.core.streams.WriteStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class RecordParserWriteStream implements WriteStream<Buffer> {

    private final RecordParser recordParser;

    @Override
    public WriteStream<Buffer> exceptionHandler(@Nullable Handler<Throwable> handler) {
        recordParser.exceptionHandler(handler);
        return this;
    }

    @Override
    public void write(Buffer data, Handler<AsyncResult<Void>> handler) {
        log.info("write {}", data.length());
        recordParser.handle(data);
        Promise<Void> promise = Promise.promise();
        promise.complete();
        handler.handle(promise.future());
    }

    @Override
    public void end(Handler<AsyncResult<Void>> handler) {
        Promise<Void> promise = Promise.promise();
        promise.complete();
        handler.handle(promise.future());
    }

    @Override
    public boolean writeQueueFull() {
        return false;
    }

    @Override
    public WriteStream<Buffer> drainHandler(@Nullable Handler<Void> handler) {
        return this;
    }

    @Override
    public Future<Void> write(Buffer data) {
        throw new UnsupportedOperationException();
    }

    @Override
    public WriteStream<Buffer> setWriteQueueMaxSize(int maxSize) {
        throw new UnsupportedOperationException();
    }
}
