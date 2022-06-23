package com.spring.webflux.todo.dto;

import java.io.Serializable;
import java.util.Set;
import lombok.*;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Section implements Serializable {
  private final String name;
  private Set<Integer> tagIds = Set.of(); // default no tags

  @Getter(AccessLevel.NONE)
  private Integer parentSectionId = DefaultSection.DEFAULT.getId();
}
