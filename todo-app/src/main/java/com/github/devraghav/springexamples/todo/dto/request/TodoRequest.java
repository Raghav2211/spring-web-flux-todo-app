package com.github.devraghav.springexamples.todo.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.*;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@RequiredArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@Scope(scopeName = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class TodoRequest implements Serializable {

  @EqualsAndHashCode.Include private final String task;
  @EqualsAndHashCode.Include private final Schedule schedule;

  @Getter(AccessLevel.NONE)
  private Set<String> tags = Set.of();

  public Set<String> getTags() {
    return Optional.ofNullable(tags).orElseGet(() -> Set.of()).stream()
        .map(String::toUpperCase)
        .collect(Collectors.toSet());
  }
}
