package com.github.devraghav.springexamples.todo.exception;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

// spotless:off
/**
 * {
 *   "timestamp": "2022-07-05T14:04:57.129+00:00",
 *   "path": "/api/v2/2379da84b8924b1b9cb73440084e46c/todo",
 *   "status": 500,
 *   "error": "Internal Server Error",
 *   "message": "Invalid section",
 *   "requestId": "149b21b3-33"
 * }
 */

// spotless:on
@RestControllerAdvice
public class TodoExceptionHandler {

  @ExceptionHandler(TodoRuntimeException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public @ResponseBody Mono<ErrorResponse> handleTodoRuntimeException(
      final TodoRuntimeException exception) {
    return Mono.just(exception).map(this::getErrorResponse);
  }

  @ExceptionHandler(InvalidTodoException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public @ResponseBody Mono<ErrorResponse> handleInvalidTodoException(
      final InvalidTodoException invalidTodoException) {
    return Mono.just(invalidTodoException)
        .map(exception -> getErrorResponse(exception, Map.of("id", exception.getTodoId())));
  }

  @ExceptionHandler(InvalidSectionRuntimeException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public @ResponseBody Mono<ErrorResponse> handleInvalidSectionRuntimeException(
      final InvalidSectionRuntimeException invalidSectionRuntimeException) {
    return Mono.just(invalidSectionRuntimeException)
        .map(exception -> getErrorResponse(exception, Map.of("sectionId", exception.getSection())));
  }

  private ErrorResponse getErrorResponse(Exception exception) {
    return getErrorResponse(exception, Map.of());
  }

  private ErrorResponse getErrorResponse(Exception exception, Map<String, Object> meta) {
    var errorResponse = new ErrorResponse();
    errorResponse.setMessage(exception.getMessage());
    errorResponse.setMeta(meta);
    return errorResponse;
  }
}
