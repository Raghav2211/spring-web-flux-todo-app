package com.spring.webflux.todo.dto;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Schedule implements Serializable {

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "yyyyMMddHHmm")
  private final LocalDateTime start;

  private List<DayOfWeek> dayOfWeeks = List.of(); // default no repeat

  public boolean isRepeat() {
    return CollectionUtils.isNotEmpty(dayOfWeeks);
  }
}
