package com.spring.webflux.todo.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "todo")
public class Todo {
  @Id private String id;
  private String content;
}
