package com.spring.webflux.todo.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class InvalidSectionRuntimeException extends RuntimeException {
  private final String section;
}
