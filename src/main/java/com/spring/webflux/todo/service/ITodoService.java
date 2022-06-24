package com.spring.webflux.todo.service;

import com.spring.webflux.todo.dto.TodoRequest;
import com.spring.webflux.todo.entity.Todo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ITodoService {

  public Mono<Todo> create(Mono<TodoRequest> todo);

  public Mono<Todo> update(Mono<TodoRequest> todo, Integer id);

  public Mono<Todo> findById(Integer id);

  public Flux<Todo> findAll();

  public Mono<Void> delete(Integer id);
}
