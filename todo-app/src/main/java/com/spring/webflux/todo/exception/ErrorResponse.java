package com.spring.webflux.todo.exception;

import java.util.Map;
import lombok.Data;

@Data
public class ErrorResponse {

  private String message;
  private Map<String, Object> meta;
}
