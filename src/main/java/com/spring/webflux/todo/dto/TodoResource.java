package com.spring.webflux.todo.dto;

import java.io.Serializable;
import java.util.Set;
import lombok.Data;

@Data
public class TodoResource implements Serializable {

  private String content;
  private Integer sectionId = DefaultSection.DEFAULT.getId();
  private Schedule schedule;
  private Boolean isComplete = false;
  private Set<Integer> tagIds;
  private Boolean inheritParentSectionTags = false;

  private boolean isDefaultSection() {
    return sectionId == null;
  }
}
