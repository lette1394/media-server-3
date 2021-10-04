package com.github.lette1394.storage.usecase;

public class CannotUploadException extends RuntimeException {
  public CannotUploadException() {
    super();
  }

  public CannotUploadException(String message) {
    super(message);
  }
}
