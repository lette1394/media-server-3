package com.github.lette1394.storage.infra;

import com.github.lette1394.storage.domain.BinaryPublisher;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ThreadLocalRandom;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.reactivestreams.Subscriber;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
public final class BrokenPublisher implements BinaryPublisher {
  private final ByteBufAllocator byteBufAllocator;

  @Override
  public void subscribe(Subscriber<? super ByteBuf> s) {
    final var inputStream = brokenInputStream();
    Flux.<ByteBuf>generate(sink -> {
        final ByteBuf buf = byteBufAllocator.buffer();
        final byte[] array = new byte[1];

        boolean release = true;
        try {
          if ((inputStream.read(array)) >= 0) {
            release = false;
            buf.writeBytes(array);
            sink.next(buf);
          } else {
            sink.complete();
          }
        } catch (IOException e) {
          sink.error(e);
        } finally {
          if (release) {
            buf.release();
          }
        }
      })
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
