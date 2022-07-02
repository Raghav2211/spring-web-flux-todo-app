package com.spring.webflux.todo.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class Schedule implements Serializable {

  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
  private final LocalDateTime start;

  private Boolean reminder = false;
  private List<DayOfWeek> dayOfWeeks = List.of(); // default no repeat
}
