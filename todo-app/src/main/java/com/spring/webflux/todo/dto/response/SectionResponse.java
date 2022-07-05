package com.spring.webflux.todo.dto.response;

import com.spring.webflux.todo.dto.request.SectionRequest;
import lombok.Getter;

@Getter
public class SectionResponse extends SectionRequest {
  private String id;

  public SectionResponse(String id, String name) {
    super(name);
    this.id = id;
  }
}
