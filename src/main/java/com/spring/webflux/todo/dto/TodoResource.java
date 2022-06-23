package com.spring.webflux.todo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.util.Set;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class TodoResource implements Serializable {

  private String content;
  private String sectionId;
  private Schedule schedule;
  private Boolean isComplete = false;
  private Set<Tag> tags;
}
