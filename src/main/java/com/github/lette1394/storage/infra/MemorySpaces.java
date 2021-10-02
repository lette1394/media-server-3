package com.github.lette1394.storage.infra;

import com.github.lette1394.storage.domain.AllSpaces;
import com.github.lette1394.storage.domain.Space;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class MemorySpaces implements AllSpaces {
  private final Map<String, Space> holder;

  public MemorySpaces(Map<String, Space> holder) {
    this.holder = holder;
  }

  @Override
  public CompletionStage<Space> belongingTo(String name) {
    if (holder.containsKey(name)) {
      return CompletableFuture.completedFuture(holder.get(name));
    }
    return CompletableFuture.failedFuture(
      new RuntimeException("not found space: [%s]".formatted(name)));
  }
}
