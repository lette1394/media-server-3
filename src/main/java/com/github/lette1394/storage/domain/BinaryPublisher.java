package com.github.lette1394.storage.domain;

import io.netty.buffer.ByteBuf;
import org.reactivestreams.Publisher;

public interface BinaryPublisher extends Publisher<ByteBuf> {
  static BinaryPublisher adapt(Publisher<ByteBuf> origin) {
    return origin::subscribe;
  }
}
