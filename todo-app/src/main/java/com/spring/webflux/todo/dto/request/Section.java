package com.spring.webflux.todo.dto.request;

import java.io.Serializable;
import java.util.Set;
import lombok.*;

@Data
public class Section implements Serializable {
  private String name = "default";
  private Set<Todo> todos;
  private Set<String> tagIds = Set.of(); // default no tags
}
