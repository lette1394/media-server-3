package com.github.lette1394.storage.domain;

import com.github.lette1394.storage.infra.MemoryObject;
import java.util.concurrent.CompletionStage;
import org.apache.commons.lang3.RandomStringUtils;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;

public class Space<BUFFER> {
  private final String name;
  private final AllObjects<BUFFER> allObjects;

  public Space(String name, AllObjects<BUFFER> allObjects) {
    this.name = name;
    this.allObjects = allObjects;
  }

  public AllObjects<BUFFER> allObjects() {
    return allObjects;
  }

  public CompletionStage<? extends Object<DataBuffer>> createObject(
    Publisher<Payload<DataBuffer>> stream) {
    return MemoryObject.object(RandomStringUtils.randomAlphanumeric(10, 20), stream);
  }

  public CompletionStage<Void> save(Object<BUFFER> object) {
    return allObjects.save(object);
  }

  public CompletionStage<Object<BUFFER>> objectBelongingTo(String id) {
    return allObjects.belongingTo(id);
  }

  public String name() {
    return name;
  }
}
