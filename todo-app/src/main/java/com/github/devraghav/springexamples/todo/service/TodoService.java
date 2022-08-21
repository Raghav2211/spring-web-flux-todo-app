package com.github.devraghav.springexamples.todo.service;

import com.github.devraghav.springexamples.todo.dto.request.TodoRequest;
import com.github.devraghav.springexamples.todo.entity.Todo;
import com.github.devraghav.springexamples.todo.exception.InvalidTodoException;
import com.github.devraghav.springexamples.todo.exception.TodoRuntimeException;
import com.github.devraghav.springexamples.todo.mapper.TodoMapper;
import com.github.devraghav.springexamples.todo.repository.TodoRepository;
import lombok.extern.slf4j.Slf4j;
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

  public Mono<Todo> create(String sectionId, Mono<TodoRequest> todoResourceMono) {
    return validateTodoRequestContent(todoResourceMono)
        .map(todoRequest -> TodoMapper.INSTANCE.requestToEntity(sectionId, todoRequest))
        .flatMap(todo -> todoRepository.save(todo));
  }

  public Mono<Todo> update(String sectionId, String id, Mono<TodoRequest> todoResourceMono) {
    return validateTodoRequestContent(todoResourceMono)
        .flatMap(
            todoResource ->
                findBySectionIdAndId(sectionId, id).flatMap(todo -> todoRepository.save(todo)));
  }

  public Mono<Todo> findBySectionIdAndId(String sectionId, String id) {
    return todoRepository
        .findBySectionIdAndId(sectionId, id)
        .switchIfEmpty(Mono.error(new InvalidTodoException(id)))
        .subscribeOn(Schedulers.boundedElastic());
  }

  public Flux<Todo> findAllBySectionId(String sectionId) {
    return todoRepository.findAllBySectionId(sectionId);
  }

  public Mono<Boolean> existsBySectionIdAndId(String sectionId, String todoId) {
    return todoRepository.existsById(todoId);
  }

  public Mono<Void> disable(String id) {
    return Mono.just(id).flatMap(todoId -> todoRepository.disableTodo(todoId));
  }

  public Mono<Void> delete(String userId, String id) {
    return Mono.just(id).flatMap(todoId -> todoRepository.deleteById(todoId));
  }

  private Mono<TodoRequest> validateTodoRequestContent(Mono<TodoRequest> todoResourceMono) {
    return todoResourceMono
        .filter(todoResource -> StringUtils.hasText(todoResource.getTask()))
        .switchIfEmpty(
            Mono.error(new TodoRuntimeException(String.format("Todo content cannot be empty"))));
  }
}
