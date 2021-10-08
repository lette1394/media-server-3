package com.github.lette1394.storage.infra;

import com.github.lette1394.storage.domain.AllObjects;
import com.github.lette1394.storage.domain.Object;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import org.springframework.core.io.buffer.DataBuffer;

public class MemoryObjects implements AllObjects<DataBuffer> {
  private final Map<String, Object<DataBuffer>> holder;

  public MemoryObjects(Map<String, Object<DataBuffer>> holder) {
    this.holder = holder;
  }

  @Override
  public CompletionStage<Object<DataBuffer>> belongingTo(String id) {
    if (holder.containsKey(id)) {
      return CompletableFuture.completedFuture(holder.get(id));
    }
    return CompletableFuture.failedFuture(new RuntimeException("not found object: [%s]".formatted(id)));
  }

  @Override
  public CompletionStage<Void> save(Object<DataBuffer> object) {
    holder.put(object.id(), object);
    return CompletableFuture.completedFuture(null);
  }
}
