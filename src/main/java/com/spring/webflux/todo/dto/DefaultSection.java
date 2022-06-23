package com.spring.webflux.todo.dto;

import java.util.Locale;
import java.util.Set;
import lombok.Getter;

@Getter
public enum DefaultSection {
  DEFAULT(1);

  private Integer id;
  private Set<Integer> tagIds;
  private Section section;

  DefaultSection(Integer id) {
    this.id = id;
    tagIds = Set.<Integer>of();
    section = new Section(this.name().toLowerCase(Locale.ROOT), tagIds, null);
  }
}
