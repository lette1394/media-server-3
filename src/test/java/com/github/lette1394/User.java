package com.github.lette1394;

import static com.github.lette1394.core.domain.FluentCompletionStage.peek;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.HttpStatus.CREATED;

import com.github.lette1394.storage.domain.Space;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class User {
  private final Space space;
  private final WebClient webClient;

  public User(Space space, WebClient webClient) {
    this.space = space;
    this.webClient = webClient;
  }

  public Object create(byte[] bytes) {
    return webClient
      .post()
      .uri("/spaces/{space}", space.name())
      .body(Mono.just(bytes), byte[].class)
      .retrieve()
      .toBodilessEntity()
      .toFuture()
      .thenApply(Response::new)
      .thenApply(peek(response -> assertThat(response.statusCode(), is(CREATED))))
      .thenApply(this::toObject)
      .join();
  }

  private Object toObject(Response<Void> response) {
    return new Object(space, response.header("x-media-server-object-id"), webClient);
  }
}
