package com.github.lette1394.storage.domain;

import java.util.concurrent.CompletionStage;

public interface AllObjects<BUFFER> {
  CompletionStage<Object<BUFFER>> belongingTo(String id);

  CompletionStage<Void> save(Object<BUFFER> object);
}
