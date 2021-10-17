package com.github.lette1394.storage.infra;

import static java.util.concurrent.CompletableFuture.completedFuture;

import com.github.lette1394.storage.domain.BinaryPublisher;
import com.github.lette1394.storage.domain.Object;
import com.github.lette1394.storage.usecase.CannotUploadException;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import java.util.concurrent.CompletionStage;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.netty.ByteBufFlux;

@RequiredArgsConstructor
public class MemoryObject implements Object {
  private final String id;
  private final byte[] contents;

  public static CompletionStage<? extends Object> object(String id, BinaryPublisher contents) {

    // FIXME (jaeeun) 2021/10/18: allocator 밖에서 받기
    final var byteBufs = new CompositeByteBuf(ByteBufAllocator.DEFAULT, false, Integer.MAX_VALUE);
    return Flux
      .from(contents)
      .reduce(byteBufs, CompositeByteBuf::addComponent)
      .map(buf -> {
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        buf.release();
        return bytes;
      })
      .map(bytes -> new MemoryObject(id, bytes))
      .toFuture()
      .exceptionally(e -> {
        byteBufs.release();
        throw new CannotUploadException("업로드에 문제가 있음", e);
      });
  }

  @Override
  public String id() {
    return id;
  }

  @Override
  public CompletionStage<BinaryPublisher> contents() {
    return completedFuture(ByteBufFlux
      .fromInbound(Flux.fromArray(wrap(contents)))
      .as(BinaryPublisher::adapt));
  }

  private static Byte[] wrap(byte[] binary) {
    final int length = binary.length;
    final Byte[] result = new Byte[length];

    for (int i = 0; i < binary.length; i++) {
      result[i] = binary[i];
    }
    return result;
  }
}
