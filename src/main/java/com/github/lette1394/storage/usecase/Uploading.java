package com.github.lette1394.storage.usecase;

import com.github.lette1394.storage.domain.BinaryPublisher;
import com.github.lette1394.storage.domain.Object;
import java.util.concurrent.CompletionStage;

public interface Uploading {
  CompletionStage<Object> upload(String spaceName, BinaryPublisher contents);
}
