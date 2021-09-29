package com.github.lette1394.storage.infra;


import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Configuration
class UploadRouter {

  @Bean
  public RouterFunction<ServerResponse> upload() {
    return route(GET("/hello")
      .and(accept(MediaType.APPLICATION_JSON)), this::hello);
  }

  public Mono<ServerResponse> hello(ServerRequest request) {
    return ServerResponse.ok()
      .contentType(MediaType.APPLICATION_JSON)
      .body(BodyInserters.fromValue("Hello, Spring!"));
  }
}
