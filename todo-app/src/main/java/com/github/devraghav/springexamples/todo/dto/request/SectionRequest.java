package com.github.devraghav.springexamples.todo.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.*;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Scope(scopeName = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SectionRequest {
  private final String name;

  @Getter(AccessLevel.NONE)
  private Set<String> tags;

  public Set<String> getTags() {
    return Optional.ofNullable(tags).stream()
        .flatMap(nonEmptyTagSet -> nonEmptyTagSet.stream())
        .map(String::toUpperCase)
        .collect(Collectors.toSet());
  }
}
