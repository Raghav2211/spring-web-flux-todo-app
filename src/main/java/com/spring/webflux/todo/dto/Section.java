package com.spring.webflux.todo.dto;

import java.io.Serializable;
import java.util.Set;
import lombok.Data;

@Data
public class Section implements Serializable {
  private String section;
  private Set<Tag> tags = Set.of(); // default no tags
}
