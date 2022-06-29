package com.spring.webflux.todo.entity;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "section")
public class Section {
  private String id;
  private String name;
  private Set<Tag> tags;
}
