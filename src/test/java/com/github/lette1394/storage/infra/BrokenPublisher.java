package com.github.lette1394.storage.infra;

import com.github.lette1394.storage.domain.Payload;
import java.io.InputStream;
import java.util.concurrent.ThreadLocalRandom;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;

@RequiredArgsConstructor
public final class BrokenPublisher implements Publisher<Payload> {
  private final DataBufferFactory dataBufferFactory;

  @Override
  public void subscribe(Subscriber<? super Payload> s) {
    DataBufferUtils
      .readInputStream(this::brokenInputStream, dataBufferFactory, 1024)
      .map(DataBufferPayload::new)
      .subscribe(s);
  }

  @NotNull
  private InputStream brokenInputStream() {
    return new InputStream() {
      final int failedIndex = ThreadLocalRandom.current().nextInt(0, 1024 * 1024);
      int index = 0;

      @Override
      public int read() {
        if (index == failedIndex) {
          throw new BrokenIOException("broken");
        }
        index++;
        return ThreadLocalRandom.current().nextInt();
      }
    };
  }
}
