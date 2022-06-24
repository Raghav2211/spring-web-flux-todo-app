package com.spring.webflux.todo.exception;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TodoExceptionHandler {

  private static final String MONGO_UNAVAILABLE = "Mongo Unavailable..";

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public @ResponseBody ResponseEntity handleException(final Exception exception) {
    exception.printStackTrace();
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
  }

  @ExceptionHandler(TodoRuntimeException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public @ResponseBody ResponseEntity handleTodoRuntimeException(
      final TodoRuntimeException exception) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
  }

  @ExceptionHandler(InvalidTodoException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public @ResponseBody ResponseEntity handleInvalidTodoException(
      final InvalidTodoException exception) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .header("id", String.valueOf(exception.getTodoId()))
        .body(exception.getMessage());
  }

  @ExceptionHandler(EmptyResultDataAccessException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public @ResponseBody ResponseEntity handleDataAaccessException(
      final EmptyResultDataAccessException exception) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
  }

  @ExceptionHandler(TransactionException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public @ResponseBody ResponseEntity handleConnectionException(
      final TransactionException exception) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(MONGO_UNAVAILABLE);
  }
}
