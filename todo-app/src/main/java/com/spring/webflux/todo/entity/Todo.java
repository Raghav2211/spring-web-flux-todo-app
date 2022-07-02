package com.spring.webflux.todo.entity;

import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Todo {
  @Id private String id;
  private String sectionId;
  @EqualsAndHashCode.Include private String task;
  @EqualsAndHashCode.Include private Schedule schedule;
  private Set<String> tags;
}
