package com.github.lette1394.storage.domain;

import java.util.concurrent.CompletionStage;

public interface Object {
  String id();

  CompletionStage<BinaryPublisher> contents();
}
