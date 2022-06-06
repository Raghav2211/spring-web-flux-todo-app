package com.spring.webflux.todo.exception;

import com.spring.webflux.todo.entity.Todo;
import javax.persistence.PersistenceException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.TransactionException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TodoExceptionHandler {

  private static final String MY_SQL_UNAVAILABLE = "MySQL Unavailable..";

  @ExceptionHandler(Exception.class)
  @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
  public @ResponseBody TodoException handleException(final Exception exception) {
    TodoException error = new TodoException();
    error.setMessage(exception.getMessage());
    error.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    return error;
  }

  @ExceptionHandler(EmptyResultDataAccessException.class)
  @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
  public @ResponseBody TodoException handleDataAaccessException(final Exception exception) {
    TodoException error = new TodoException();
    error.setMessage(
        exception.getMessage().replace("class " + Todo.class.getName() + " entity", "Todo"));
    error.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    return error;
  }

  @ExceptionHandler(TransactionException.class)
  @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
  public @ResponseBody TodoException handleConnectionException(final Exception exception) {

    TodoException error = new TodoException();
    error.setMessage(MY_SQL_UNAVAILABLE);
    error.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());

    return error;
  }

  @ExceptionHandler(PersistenceException.class)
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  public @ResponseBody TodoException handlePersistence(final Exception exception) {

    TodoException error = new TodoException();
    error.setMessage(exception.getMessage());
    error.setCode(HttpStatus.NOT_FOUND.value());

    return error;
  }
}
