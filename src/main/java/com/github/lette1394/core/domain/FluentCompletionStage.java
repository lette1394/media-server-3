package com.github.lette1394.core.domain;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;
import java.util.function.Consumer;
import java.util.function.Function;
import lombok.SneakyThrows;

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

  public static <T> Function<T, CompletionStage<T>> peekStage(
    Function<T, CompletionStage<? extends Void>> voidStageFunction) {
    return t -> voidStageFunction.apply(t).thenApply(__ -> t);
  }

  @SneakyThrows
  public static <T> T await(CompletionStage<T> stage) {
    try {
      return stage.toCompletableFuture().join();
    } catch (CompletionException e) {
      final var cause = e.getCause();
      if (cause == null) {
        throw e;
      }
      throw cause;
    }
  }
}
