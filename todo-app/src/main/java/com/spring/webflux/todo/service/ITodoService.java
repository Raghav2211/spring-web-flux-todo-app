package com.spring.webflux.todo.service;

import com.spring.webflux.todo.dto.request.TodoRequest;
import com.spring.webflux.todo.entity.Todo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ITodoService {

  public Mono<Todo> create(String sectionId, Mono<TodoRequest> todo);

  public Mono<Todo> update(String sectionId, String id, Mono<TodoRequest> todo);

  public Mono<Todo> findBySectionIdAndId(String sectionId, String todoId);

  public Flux<Todo> findAllBySectionId(String sectionId);

  public Mono<Void> delete(String sectionId, String id);
}
