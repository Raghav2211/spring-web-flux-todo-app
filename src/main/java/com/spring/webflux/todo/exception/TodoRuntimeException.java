package com.spring.webflux.todo.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
public class TodoRuntimeException extends RuntimeException {
  private final HttpStatus httpStatus;
  private String message = "Internal server error";
}
