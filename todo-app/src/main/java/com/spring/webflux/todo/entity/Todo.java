package com.spring.webflux.todo.entity;

import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "todo")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Todo {
  @Id private String id;
  private String sectionId;
  @EqualsAndHashCode.Include private String task;
  @EqualsAndHashCode.Include private Schedule schedule;
  @EqualsAndHashCode.Include private Boolean isActive = true;
  private Set<String> tags;
}
