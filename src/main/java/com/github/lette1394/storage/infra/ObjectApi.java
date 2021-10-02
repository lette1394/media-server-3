package com.github.lette1394.storage.infra;

import static com.github.lette1394.core.domain.FluentCompletionStage.start;
import static org.springframework.http.ResponseEntity.notFound;

import com.github.lette1394.storage.domain.AllSpaces;
import com.github.lette1394.storage.domain.Object;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class ObjectApi {
  private final AllSpaces allSpaces;

  public ObjectApi(AllSpaces allSpaces) {
    this.allSpaces = allSpaces;
  }

  @PostMapping("/spaces/{spaceName}")
  Mono<ResponseEntity<?>> upload(
    ServerHttpRequest request,
    @PathVariable String spaceName) {

    final var response = allSpaces
      .belongingTo(spaceName)
      .thenCompose(space -> space.createObject(request.getBody())
        .thenCompose(object -> space.save(object).thenApply(__ -> object)))
      .thenApply(Object::id)
      .thenApply(id -> ResponseEntity
        .status(HttpStatus.CREATED)
        .header("x-media-server-object-id", id)
        .build())
      .exceptionally(__ -> notFound().build());

    return Mono.fromCompletionStage(response);
  }

  @GetMapping("/spaces/{spaceName}/objects/{objectId}")
  Mono<ResponseEntity<Publisher<DataBuffer>>> download(
    ServerHttpRequest request,
    @PathVariable String spaceName,
    @PathVariable String objectId) {

    final var response = start()
      .thenCompose(__ -> allSpaces.belongingTo(spaceName))
      .thenCompose(space -> space.objectBelongingTo(objectId))
      .thenApply(Object::contents)
      .thenApply(contents -> ResponseEntity
        .status(HttpStatus.OK)
        .contentType(MediaType.IMAGE_JPEG)
        .body(contents))
      .exceptionally(__ -> notFound().build());

    return Mono.fromCompletionStage(response);
  }
}
