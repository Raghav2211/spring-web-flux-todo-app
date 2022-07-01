package com.spring.webflux.todo.entity;

import org.springframework.data.mongodb.core.mapping.Document;

// @Data
@Document(collection = "userTodos")
public class UserTodoList {
  private String id;
  private Section section;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Section getSection() {
    return section;
  }

  public void setSection(Section section) {
    this.section = section;
  }
}
