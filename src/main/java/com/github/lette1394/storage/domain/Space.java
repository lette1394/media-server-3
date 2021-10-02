package com.github.lette1394.storage.domain;

import lombok.RequiredArgsConstructor;

public class Space {
  private final String name;

  public Space(String name) {
    this.name = name;
  }

  public static Space space(String name) {
    return new Space(name);
  }

  public String name() {
    return name;
  }
}
