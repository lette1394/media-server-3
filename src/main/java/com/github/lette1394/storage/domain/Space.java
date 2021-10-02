package com.github.lette1394.storage.domain;

import com.github.lette1394.storage.infra.MemoryObject;
import java.util.concurrent.CompletionStage;
import org.apache.commons.lang3.RandomStringUtils;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;

public class Space {
  private final String name;
  private final AllObjects allObjects;

  public Space(String name, AllObjects allObjects) {
    this.name = name;
    this.allObjects = allObjects;
  }

  public CompletionStage<? extends Object> createObject(Publisher<DataBuffer> stream) {
    return MemoryObject.object(RandomStringUtils.randomAlphanumeric(10, 20), stream);
  }

  public CompletionStage<Void> save(Object object) {
    return allObjects.save(object);
  }

  public CompletionStage<Object> objectBelongingTo(String id) {
    return allObjects.belongingTo(id);
  }

  public String name() {
    return name;
  }
}
