package com.github.lette1394;

import static com.github.lette1394.core.domain.FluentCompletionStage.peek;
import static java.util.Objects.requireNonNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.reactive.function.BodyExtractors.toDataBuffers;

import java.util.concurrent.CompletionStage;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

public class Object {
  private final Space space;
  private final String id;
  private final WebClient webClient;

  public Object(Space space, String id, WebClient webClient) {
    this.space = space;
    this.id = id;
    this.webClient = webClient;
  }

  public byte[] contents() {
    return webClient
      .get()
      .uri("/spaces/{space}/objects/{objectId}", space.name(), id)
      .retrieve()
      .toEntityFlux(toDataBuffers())
      .toFuture()
      .thenApply(Response::new)
      .thenApply(peek(entity -> assertThat(entity.statusCode(), is(OK))))
      .thenCompose(this::toByteArray)
      .join();
  }

  private CompletionStage<byte[]> toByteArray(Response<Flux<DataBuffer>> entity) {
    return DataBufferUtils
      .join(requireNonNull(entity.body()))
      .map(dataBuffer -> {
        byte[] bytes = new byte[dataBuffer.readableByteCount()];
        dataBuffer.read(bytes);
        DataBufferUtils.release(dataBuffer);
        return bytes;
      })
      .toFuture();
  }
}
