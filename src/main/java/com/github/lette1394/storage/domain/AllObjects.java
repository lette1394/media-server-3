package com.github.lette1394.storage.domain;

import java.util.concurrent.CompletionStage;

public interface AllObjects {
  CompletionStage<Object> belongingTo(String id);

  CompletionStage<Void> save(Object object);
}
