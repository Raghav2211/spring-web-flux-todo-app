package com.github.devraghav.springexamples.todo.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class InvalidTodoException extends TodoRuntimeException {
  private final String todoId;

  public InvalidTodoException(String todoId) {
    super("Todo not found");
    this.todoId = todoId;
  }
}
