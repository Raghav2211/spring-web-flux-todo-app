package com.spring.webflux.todo.exception;

import lombok.Getter;

@Getter
public class InvalidSectionRuntimeException extends RuntimeException {
  private final String section;

  public InvalidSectionRuntimeException(String section) {
    super("Invalid section");
    this.section = section;
  }
}
