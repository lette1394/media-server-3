package com.github.lette1394.storage.infra;

import static java.util.Objects.requireNonNull;

import com.github.lette1394.storage.domain.Object;
import java.io.ByteArrayInputStream;
import java.util.concurrent.CompletionStage;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;

public class MemoryObject implements Object {
  private final String id;
  private final byte[] contents;

  public MemoryObject(String id, byte[] contents) {
    this.id = id;
    this.contents = contents;
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
      .toFuture();
  }

  @Override
  public String id() {
    return id;
  }

  @Override
  public Publisher<DataBuffer> contents() {
    return DataBufferUtils.readInputStream(() -> new ByteArrayInputStream(contents), new DefaultDataBufferFactory(), 1024);
  }
}
