package com.spring.webflux.todo.service;

import com.spring.webflux.todo.dto.request.Request;
import com.spring.webflux.todo.entity.UserTodoList;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ITodoService {

  public Mono<UserTodoList> create(String userId, Mono<Request> todo);

  public Mono<UserTodoList> update(String userId, Mono<Request> todo, Integer id);

  public Mono<UserTodoList> findById(Integer id);

  public Flux<UserTodoList> findAll();

  public Mono<Void> delete(String userId, Integer id);
}
