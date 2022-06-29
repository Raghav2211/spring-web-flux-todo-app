package com.spring.webflux.todo.dto;

import java.util.Locale;
import java.util.Optional;
import lombok.Getter;

@Getter
public enum StandardTags {
  IMPORTANT(1, "Todo for important work"),
  HOME(2, "Tag for home "),
  WORK(3, "Todo for workplace");
  private Integer id;
  private String description;
  private Tag tag;

  StandardTags(Integer id, String description) {
    this.id = id;
    this.description = description;
    tag = new Tag(this.name().toLowerCase(Locale.ROOT), Optional.of(description));
  }
}
