package com.spring.webflux.todo.dto;

import java.io.Serializable;
import java.util.Set;
import lombok.Data;

@Data
public class Section implements Serializable {
  private final String name;
  private Set<Integer> tagIds = Set.of(); // default no tags
  private Section parent = DefaultSection.DEFAULT.getSection(); // default section as parent section
}
