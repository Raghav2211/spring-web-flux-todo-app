package com.github.devraghav.springexamples.todo.mapper;

import java.util.UUID;

public final class MappingUtils {

  public static final String GENERATE_UUID_EXPRESSION =
      "java(com.github.devraghav.springexamples.todo.mapper.MappingUtils.generateUuid())";

  public static String generateUuid() {
    return UUID.randomUUID().toString().replace("-", "");
  }
}
