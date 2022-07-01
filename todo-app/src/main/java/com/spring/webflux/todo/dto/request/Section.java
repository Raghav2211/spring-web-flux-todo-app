package com.spring.webflux.todo.dto.request;

import java.io.Serializable;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.*;

@Data
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Section implements Serializable {
  @EqualsAndHashCode.Include private final String name = "default";
  @EqualsAndHashCode.Include private final Set<Todo> todos;

  @Getter(AccessLevel.NONE)
  private Set<String> tags = Set.of();

  public Set<String> getTags() {
    return Optional.ofNullable(tags).orElseGet(() -> Set.of()).stream()
        .map(String::toUpperCase)
        .collect(Collectors.toSet());
  }
}
