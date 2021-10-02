package com.github.lette1394.core.domain;

public class Contracts {
  public static void requires(boolean truthy) {
    if (truthy) {
      return;
    }

    throw new ContractsViolationException();
  }

  private static final class ContractsViolationException extends RuntimeException {

  }
}
