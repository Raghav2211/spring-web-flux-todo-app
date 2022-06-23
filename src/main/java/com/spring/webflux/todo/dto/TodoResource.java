package com.spring.webflux.todo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.ImmutableSet;
import java.io.Serializable;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class TodoResource implements Serializable {

  private String userId = "1";
  private String content;
  private Section section;
  private Schedule schedule;
  private Boolean isComplete = false;

  @Getter(AccessLevel.NONE)
  private Set<Tag> tags;

  public Set<Tag> getTags() {
    return ImmutableSet.<Tag>builder().addAll(getTags()).addAll(section.getTags()).build();
  }
}
