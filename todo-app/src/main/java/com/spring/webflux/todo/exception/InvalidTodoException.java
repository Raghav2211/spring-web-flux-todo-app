package com.spring.webflux.todo.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@EqualsAndHashCode(callSuper = true)
public class InvalidTodoException extends TodoRuntimeException {
  private final Integer todoId;
  private static final HttpStatus httpStatus = HttpStatus.NOT_FOUND;

  public InvalidTodoException(Integer todoId, String message) {
    super(httpStatus, message);
    this.todoId = todoId;
  }

  public InvalidTodoException(Integer todoId) {
    super(httpStatus);
    this.todoId = todoId;
  }
}
