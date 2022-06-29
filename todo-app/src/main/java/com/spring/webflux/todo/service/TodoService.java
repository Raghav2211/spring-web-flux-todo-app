package com.spring.webflux.todo.service;

import com.spring.webflux.todo.dto.TodoRequest;
import com.spring.webflux.todo.entity.Todo;
import com.spring.webflux.todo.exception.InvalidTodoException;
import com.spring.webflux.todo.exception.TodoRuntimeException;
import com.spring.webflux.todo.repository.TodoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@Transactional
@Slf4j
public class TodoService implements ITodoService {

  private TodoRepository todoRepository;

  public TodoService(TodoRepository todoRepository) {
    this.todoRepository = todoRepository;
  }

  public Mono<Todo> create(Mono<TodoRequest> todoResourceMono) {
    return validateTodoRequestContent(todoResourceMono)
        .map(this::mapToTodo)
        .flatMap(todo -> todoRepository.save(todo));
  }

  public Mono<Todo> update(Mono<TodoRequest> todoResourceMono, Integer id) {
    return validateTodoRequestContent(todoResourceMono)
        .flatMap(
            todoResource ->
                findById(id)
                    .doOnNext(todo -> updateExistingTodo(todo, todoResource))
                    .flatMap(todo -> todoRepository.save(todo)));
  }

  private void updateExistingTodo(Todo todo, TodoRequest todoResource) {
    todo.setContent(todoResource.getContent());
  }

  public Mono<Todo> findById(Integer id) {
    return todoRepository
        .findById(id)
        .switchIfEmpty(Mono.error(new InvalidTodoException(id, "Todo not found")))
        .subscribeOn(Schedulers.boundedElastic());
  }

  public Flux<Todo> findAll() {
    return todoRepository.findAll();
  }

  public Mono<Void> delete(Integer id) {
    return Mono.just(id).flatMap(todoId -> todoRepository.deleteById(todoId));
  }

  private Mono<TodoRequest> validateTodoRequestContent(Mono<TodoRequest> todoResourceMono) {
    return todoResourceMono
        .filter(todoResource -> StringUtils.hasText(todoResource.getContent()))
        .switchIfEmpty(
            Mono.error(
                new TodoRuntimeException(
                    HttpStatus.BAD_REQUEST, String.format("Todo content cannot be empty"))));
  }

  public Todo mapToTodo(TodoRequest todoResource) {
    Todo todo = new Todo();
    todo.setContent(todoResource.getContent());
    return todo;
  }
}
