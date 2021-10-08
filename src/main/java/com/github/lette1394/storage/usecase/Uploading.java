package com.github.lette1394.storage.usecase;

import com.github.lette1394.storage.domain.Object;
import com.github.lette1394.storage.domain.Payload;
import java.util.concurrent.CompletionStage;
import org.reactivestreams.Publisher;

public interface Uploading<BUFFER> {
  CompletionStage<Object<BUFFER>> upload(String spaceName, Publisher<Payload<BUFFER>> contents);
}
