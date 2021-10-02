package com.github.lette1394.storage.domain;

import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;

public interface Object {
  String id();

  Publisher<DataBuffer> contents();
}
