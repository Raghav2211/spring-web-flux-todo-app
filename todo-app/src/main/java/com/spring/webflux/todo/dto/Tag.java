package com.spring.webflux.todo.dto;

import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Tag {
  private final String name;
  private Optional<String> description;
}
