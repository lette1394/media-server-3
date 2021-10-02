package com.github.lette1394;

import static com.github.lette1394.core.domain.Contracts.requires;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class Response<T> {
  private final ResponseEntity<T> entity;

  public Response(ResponseEntity<T> entity) {
    this.entity = entity;
  }

  public HttpStatus statusCode() {
    return entity.getStatusCode();
  }

  public T body() {
    return entity.getBody();
  }

  public String header(String name) {
    final var headers = entity.getHeaders();
    requires(headers.containsKey(name));
    final var headerValues = headers.get(name);
    //noinspection ConstantConditions
    requires(headerValues.size() == 1);
    return headerValues.get(0);
  }
}
