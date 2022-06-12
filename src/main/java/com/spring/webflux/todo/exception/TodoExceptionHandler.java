package com.spring.webflux.todo.exception;

import javax.persistence.PersistenceException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TodoExceptionHandler {

  private static final String MY_SQL_UNAVAILABLE = "MySQL Unavailable..";

  @ExceptionHandler(Exception.class)
  public @ResponseBody ResponseEntity handleException(final Exception exception) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
  }

  @ExceptionHandler(TodoRuntimeException.class)
  public @ResponseBody ResponseEntity handleTodoRuntimeException(
      final TodoRuntimeException exception) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
  }

  @ExceptionHandler(InvalidTodoException.class)
  public @ResponseBody ResponseEntity handleInvalidTodoException(
      final InvalidTodoException exception) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .header("id", String.valueOf(exception.getTodoId()))
        .body(exception.getMessage());
  }

  @ExceptionHandler(EmptyResultDataAccessException.class)
  public @ResponseBody ResponseEntity handleDataAaccessException(
      final EmptyResultDataAccessException exception) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
  }

  @ExceptionHandler(TransactionException.class)
  public @ResponseBody ResponseEntity handleConnectionException(
      final TransactionException exception) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(MY_SQL_UNAVAILABLE);
  }

  @ExceptionHandler(PersistenceException.class)
  public @ResponseBody ResponseEntity handlePersistence(final PersistenceException exception) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
  }
}
