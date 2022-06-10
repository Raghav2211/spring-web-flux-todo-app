package com.spring.webflux.todo.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TodoNotFoundException extends RuntimeException {
  private final Long todoId;
}
