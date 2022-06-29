package com.spring.webflux.todo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.util.Set;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Scope(scopeName = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class TodoRequest implements Serializable {

  private String content;
  private Integer sectionId = DefaultSection.DEFAULT.getId();
  private Schedule schedule;
  private Set<Integer> tagIds;
  private Boolean inheritParentSectionTags = false;

  private boolean isDefaultSection() {
    return sectionId == null;
  }
}
