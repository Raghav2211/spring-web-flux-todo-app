package com.github.devraghav.springexamples.todo.entity;

import java.util.Set;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "section")
@Data
public class Section {
  @Id private String id;
  private String userId;
  private String name;
  private Set<String> tags;
}
