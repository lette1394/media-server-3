package com.github.lette1394.storage.infra;

import static com.github.lette1394.core.domain.FluentCompletionStage.start;
import static java.util.Objects.requireNonNull;

import com.github.lette1394.storage.domain.Object;
import com.github.lette1394.storage.usecase.CannotUploadException;
import java.io.ByteArrayInputStream;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;

@RequiredArgsConstructor
public class MemoryObject implements Object {
  private final String id;
  private final byte[] contents;

  public static CompletionStage<? extends Object> from(Object object) {
    return start()
      .thenCompose(__ -> object.contents())
      .thenCompose(contents -> object(object.id(), contents));
  }

  public static CompletionStage<? extends Object> object(String id, Publisher<DataBuffer> contents) {
    return DataBufferUtils
      .join(requireNonNull(contents))
      .map(dataBuffer -> {
        byte[] bytes = new byte[dataBuffer.readableByteCount()];
        dataBuffer.read(bytes);
        DataBufferUtils.release(dataBuffer);
        return bytes;
      })
      .map(bytes -> new MemoryObject(id, bytes))
      .toFuture()
      .exceptionally(e -> {
        throw new CannotUploadException("업로드에 문제가 있음", e);
      });
  }

  @Override
  public String id() {
    return id;
  }

  @Override
  public CompletionStage<Publisher<DataBuffer>> contents() {
    return CompletableFuture.completedFuture(
      DataBufferUtils.readInputStream(() -> new ByteArrayInputStream(contents), new DefaultDataBufferFactory(), 1024));
  }
}
