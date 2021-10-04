package com.github.lette1394.storage.infra;

import static com.github.lette1394.core.domain.FluentCompletionStage.peekStage;
import static com.github.lette1394.core.domain.FluentCompletionStage.start;
import static com.github.lette1394.storage.infra.MemoryObject.object;

import com.github.lette1394.storage.domain.AllSpaces;
import com.github.lette1394.storage.domain.Object;
import com.github.lette1394.storage.domain.Space;
import com.github.lette1394.storage.usecase.Uploading;
import java.util.concurrent.CompletionStage;
import org.apache.commons.lang3.RandomStringUtils;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;

public class MemoryUploading implements Uploading {
  private final AllSpaces allSpaces;

  public MemoryUploading(AllSpaces allSpaces) {
    this.allSpaces = allSpaces;
  }

  @Override
  public CompletionStage<Object> upload(String spaceName, Publisher<DataBuffer> contents) {
    return start()
      .thenCompose(__ -> allSpaces.belongingTo(spaceName))
      .thenCompose(space -> objects(space, contents));
  }

  private static CompletionStage<Object> objects(Space space, Publisher<DataBuffer> contents) {
    final var allObjects = space.allObjects();
    final var id = RandomStringUtils.randomAlphanumeric(10, 20);
    return start()
      .thenCompose(__ -> object(id, contents))
      .thenCompose(peekStage(allObjects::save));
  }
}
