package com.spring.webflux.todo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class Schedule implements Serializable {

  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime start;

  private Boolean reminder = false;
  private List<DayOfWeek> dayOfWeeks = List.of(); // default no repeat
}
