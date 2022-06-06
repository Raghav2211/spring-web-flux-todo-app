package com.spring.webflux.todo.service;

import com.spring.webflux.todo.dto.TodoResource;
import com.spring.webflux.todo.entity.Todo;
import com.spring.webflux.todo.repository.TodoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Transactional
@Slf4j
public class TodoService implements ITodoService {

  private TodoRepository todoRepository;

  public TodoService(TodoRepository todoRepository) {
    this.todoRepository = todoRepository;
  }

  public Mono<Todo> create(Mono<TodoResource> todoResourceMono) {
    return validateTodoRequestContent(todoResourceMono)
        .map(TodoService::mapToTodo)
        .flatMap(todo -> Mono.fromCallable(() -> todoRepository.save(todo)));
  }

  public Mono<Todo> update(Mono<TodoResource> todoResourceMono, Long id) {
    return validateTodoRequestContent(todoResourceMono)
        .flatMap(
            todoResource ->
                findById(id)
                    .doOnNext(todo -> updateExistingTodo(todo, todoResource))
                    .map(todo -> todoRepository.save(todo)));
  }

  private static void updateExistingTodo(Todo todo, TodoResource todoResource) {
    todo.setIsComplete(todoResource.getIsComplete());
    todo.setContent(todoResource.getContent());
  }

  public Mono<Todo> findById(Long id) {
    return Mono.justOrEmpty(todoRepository.findById(id))
        .switchIfEmpty(
            Mono.error(new IllegalArgumentException(String.format("Todo[%d] not found", id))));
  }

  public Flux<Todo> findAll() {
    return Flux.fromIterable(todoRepository.findAll());
  }

  public Mono<Void> delete(Long id) {
    return validateTodoRequestId(id)
        .flatMap(todoId -> Mono.fromRunnable(() -> todoRepository.deleteById(todoId)));
  }

  // business logics
  private Mono<TodoResource> validateTodoRequestContent(Mono<TodoResource> todoResourceMono) {
    return todoResourceMono
        .filter(todoResource -> StringUtils.hasText(todoResource.getContent()))
        .switchIfEmpty(
            Mono.error(
                new IllegalArgumentException(String.format("Todo content cannot be empty"))));
  }

  private Mono<Long> validateTodoRequestId(Long id) {
    if (id == null || id < 0) {
      return Mono.error(
          new IllegalArgumentException(String.format("Todo[%d] is not a valid id", id)));
    }
    return Mono.just(id);
  }

  public static Todo mapToTodo(TodoResource todoResource) {
    Todo todo = new Todo();
    todo.setContent(todoResource.getContent());
    todo.setIsComplete(todoResource.getIsComplete());
    return todo;
  }
}
