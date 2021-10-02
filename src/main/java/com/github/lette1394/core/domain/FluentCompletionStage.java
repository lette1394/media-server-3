package com.github.lette1394.core.domain;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Consumer;
import java.util.function.Function;

public final class FluentCompletionStage {
  public static CompletionStage<Void> start() {
    return CompletableFuture.completedFuture(null);
  }

  public static <T> Function<T, T> peek(Consumer<? super T> consumer) {
    return t -> {
      consumer.accept(t);
      return t;
    };
  }
}
