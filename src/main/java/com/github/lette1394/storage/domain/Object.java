package com.github.lette1394.storage.domain;

import java.util.concurrent.CompletionStage;
import org.reactivestreams.Publisher;

public interface Object<BUFFER> {
  String id();

  CompletionStage<? extends Publisher<Payload<BUFFER>>> contents();
}
