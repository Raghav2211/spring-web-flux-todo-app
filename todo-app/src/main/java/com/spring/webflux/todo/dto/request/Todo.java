package com.spring.webflux.todo.dto.request;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.*;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Todo {

  @EqualsAndHashCode.Include private String task;
  @EqualsAndHashCode.Include private Schedule schedule;

  @Getter(AccessLevel.NONE)
  private Set<String> tags = Set.of();

  public Set<String> getTags() {
    return Optional.ofNullable(tags).orElseGet(() -> Set.of()).stream()
        .map(String::toUpperCase)
        .collect(Collectors.toSet());
  }
}
