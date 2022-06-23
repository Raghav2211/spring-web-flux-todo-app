package com.spring.webflux.todo.dto;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Getter;

public class Tag {
  private String id;
  private String name;
  private Optional<String> description;

  @Getter
  public static enum DefaultTag {
    IMPORTANT(1, "Todo for important work"),
    HOME(2, "Tag for home "),
    WORK(3, "Todo for workplace");

    private static final Map<Integer, DefaultTag> ID_TAG_MAP =
        Arrays.stream(DefaultTag.values())
            .collect(Collectors.toUnmodifiableMap(tag -> tag.getId(), Function.identity()));

    private Integer id;
    private String description;

    DefaultTag(Integer id, String description) {
      this.id = id;
      this.description = description;
    }
  }
}
