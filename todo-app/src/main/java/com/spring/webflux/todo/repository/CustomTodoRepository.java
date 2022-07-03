package com.spring.webflux.todo.repository;

import reactor.core.publisher.Mono;

public interface CustomTodoRepository {

  Mono<Void> disableTodo(String id);
}
