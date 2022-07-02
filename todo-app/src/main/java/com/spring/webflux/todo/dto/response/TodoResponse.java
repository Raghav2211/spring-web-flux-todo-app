package com.spring.webflux.todo.dto.response;

import com.spring.webflux.todo.dto.request.Schedule;
import com.spring.webflux.todo.dto.request.TodoRequest;
import lombok.Getter;

@Getter
public class TodoResponse extends TodoRequest {
  private final String sectionId;

  public TodoResponse(String task, Schedule schedule, String sectionId) {
    super(task, schedule);
    this.sectionId = sectionId;
  }
}
