package com.github.devraghav.springexamples.todo.exception;

import java.util.Map;
import lombok.Data;

@Data
public class ErrorResponse {

  private String message;
  private Map<String, Object> meta;
}
