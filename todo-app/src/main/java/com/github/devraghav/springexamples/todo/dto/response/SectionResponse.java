package com.github.devraghav.springexamples.todo.dto.response;

import com.github.devraghav.springexamples.todo.dto.request.SectionRequest;
import lombok.Getter;

@Getter
public class SectionResponse extends SectionRequest {
  private String id;

  public SectionResponse(String id, String name) {
    super(name);
    this.id = id;
  }
}
