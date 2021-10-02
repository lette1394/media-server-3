package com.github.lette1394.storage.infra;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@RestController
public class ObjectApi {

  @PostMapping("/spaces/{space}")
  Mono<ResponseEntity<Void>> upload(
    ServerHttpRequest request,
    @PathVariable("space") String space) {

    request.getBody();
    return Mono.just(ResponseEntity
      .status(HttpStatus.CREATED)
      .header("x-media-server-object-id", "aaaaaa")
      .build());
  }

  @GetMapping("/spaces/{space}/objects/{object-id}")
  Mono<ResponseEntity<Publisher<DataBuffer>>> download(
    ServerHttpRequest request,
    @PathVariable("object-id") String objectId) {

    final var bytes = new ByteArrayInputStream("hello world".getBytes(StandardCharsets.UTF_8));
    final var stream = DataBufferUtils.readInputStream(() -> bytes, new DefaultDataBufferFactory(), 1);

    return Mono.just(ResponseEntity
      .status(HttpStatus.OK)
      .body(stream));
  }
}
