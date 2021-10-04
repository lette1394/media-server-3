package com.github.lette1394.storage.domain;

import java.util.concurrent.CompletionStage;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;

public interface Object {
  String id();

  CompletionStage<Publisher<DataBuffer>> contents();
}
