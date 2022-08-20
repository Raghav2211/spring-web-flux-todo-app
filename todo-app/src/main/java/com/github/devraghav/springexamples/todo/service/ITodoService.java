package com.github.devraghav.springexamples.todo.service;

import com.github.devraghav.springexamples.todo.dto.request.TodoRequest;
import com.github.devraghav.springexamples.todo.entity.Todo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ITodoService {

  Mono<Todo> create(String sectionId, Mono<TodoRequest> todo);

  Mono<Todo> update(String sectionId, String id, Mono<TodoRequest> todo);

  Mono<Todo> findBySectionIdAndId(String sectionId, String todoId);

  Flux<Todo> findAllBySectionId(String sectionId);

  Mono<Boolean> existsBySectionIdAndId(String sectionId, String todoId);

  Mono<Void> disable(String id);

  Mono<Void> delete(String sectionId, String id);
}
