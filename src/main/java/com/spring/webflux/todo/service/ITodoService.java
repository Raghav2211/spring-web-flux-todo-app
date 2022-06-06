package com.spring.webflux.todo.service;

import com.spring.webflux.todo.dto.TodoResource;
import com.spring.webflux.todo.entity.Todo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ITodoService {

  public Mono<Todo> create(Mono<TodoResource> todo);

  public Mono<Todo> update(Mono<TodoResource> todo, Long id);

  public Mono<Todo> findById(Long id);

  public Flux<Todo> findAll();

  public Mono<Void> delete(Long id);
}
