package com.github.lette1394.storage.usecase;

import com.github.lette1394.storage.domain.Object;
import java.util.concurrent.CompletionStage;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;

public interface Uploading {
  CompletionStage<Object> upload(String spaceName, Publisher<DataBuffer> contents);
}
