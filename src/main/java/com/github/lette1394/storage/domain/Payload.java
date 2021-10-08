package com.github.lette1394.storage.domain;

public interface Payload<T> {
  T body();

  void retain();

  void release();
}
