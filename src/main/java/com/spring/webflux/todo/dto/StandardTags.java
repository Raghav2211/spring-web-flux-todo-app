package com.spring.webflux.todo.dto;

import java.util.Locale;
import java.util.Optional;
import lombok.Getter;

@Getter
public enum StandardTags {
  IMPORTANT("Todo for important work"),
  HOME("Tag for home "),
  WORK("Todo for workplace");
  private String description;
  private Tag tag;

  StandardTags(String description) {
    this.description = description;
    tag = new Tag(this.name().toLowerCase(Locale.ROOT), Optional.of(description));
  }
}
