package com.spring.webflux.todo.service;

import com.spring.webflux.todo.dto.request.TodoRequest;
import com.spring.webflux.todo.dto.response.TodoResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ITodoService {

  public Mono<TodoResponse> create(String userId, Mono<TodoRequest> todo);

  public Mono<TodoResponse> update(String userId, Mono<TodoRequest> todo, Integer id);

  public Mono<TodoResponse> findById(Integer id);

  public Flux<TodoResponse> findAll();

  public Mono<Void> delete(String userId, String id);
}
