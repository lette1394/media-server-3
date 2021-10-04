package com.github.lette1394.storage.infra;

public class BrokenIOException extends RuntimeException {
  public BrokenIOException() {
    super();
  }

  public BrokenIOException(String message) {
    super(message);
  }
}
