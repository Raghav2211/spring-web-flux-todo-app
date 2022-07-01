package com.spring.webflux.todo.entity;

import java.util.Set;

public class Section {
  private String name;
  private Set<Todo> todos;
  private Set<String> tags;
}
