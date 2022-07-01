package com.spring.webflux.todo.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "todo")
public class UserTodoList {
  private String id;
  private Section section;
}
