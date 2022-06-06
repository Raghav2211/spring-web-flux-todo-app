package com.spring.webflux.todo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Date;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "todo")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Todo {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String content;
  private Boolean isComplete;
  private Date date;

  @PrePersist
  public void onPrePersist() {
    date = new Date();
  }

  @PreUpdate
  public void onPreUpdate() {
    date = new Date();
  }
}
