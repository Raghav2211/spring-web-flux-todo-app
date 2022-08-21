package com.github.devraghav.springexamples.todo.dto.response;

import com.github.devraghav.springexamples.todo.dto.request.Schedule;
import com.github.devraghav.springexamples.todo.dto.request.TodoRequest;
import lombok.Getter;

@Getter
public class TodoResponse extends TodoRequest {
  private final String id;
  private final Boolean isActive;
  private final String sectionId;

  public TodoResponse(
      String id, String sectionId, String task, Schedule schedule, Boolean isActive) {
    super(task, schedule);
    this.id = id;
    this.sectionId = sectionId;
    this.isActive = isActive;
  }
}
