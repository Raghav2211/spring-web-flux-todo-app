package com.spring.webflux.todo.mapper;

import java.util.UUID;

public final class MappingUtils {

  public static final String GENERATE_UUID_EXPRESSION =
      "java(com.spring.webflux.todo.mapper.MappingUtils.generateUuid())";

  public static String generateUuid() {
    return UUID.randomUUID().toString().replace("-", "");
  }
}
