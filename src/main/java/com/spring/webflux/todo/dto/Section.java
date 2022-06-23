package com.spring.webflux.todo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.util.Set;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Section implements Serializable {
  private final String name;
  private Set<Tag> tags = Set.of(); // default no tags
  private Section parent = new Section("default"); // default section as parent section
}
