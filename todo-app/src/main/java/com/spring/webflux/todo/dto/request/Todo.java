package com.spring.webflux.todo.dto.request;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Todo {
  @EqualsAndHashCode.Include private final String task;
  @EqualsAndHashCode.Include private Schedule schedule;
  private Set<String> tags;
}
