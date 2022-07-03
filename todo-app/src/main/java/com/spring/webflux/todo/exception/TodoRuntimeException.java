package com.spring.webflux.todo.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
public class TodoRuntimeException extends RuntimeException {
  private String message = "Internal server error";
}
