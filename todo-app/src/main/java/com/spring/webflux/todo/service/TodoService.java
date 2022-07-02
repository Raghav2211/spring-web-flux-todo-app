package com.spring.webflux.todo.service;

import com.spring.webflux.todo.dto.request.TodoRequest;
import com.spring.webflux.todo.entity.Todo;
import com.spring.webflux.todo.exception.InvalidTodoException;
import com.spring.webflux.todo.exception.TodoRuntimeException;
import com.spring.webflux.todo.mapper.TodoMapper;
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
        .switchIfEmpty(Mono.error(new InvalidTodoException(id, "Todo not found")))
        .subscribeOn(Schedulers.boundedElastic());
  }

  public Flux<Todo> findAllBySectionId(String sectionId) {
    return todoRepository.findAllBySectionId(sectionId);
  }

  public Mono<Void> delete(String userId, String id) {
    return Mono.just(id).flatMap(todoId -> todoRepository.deleteById(todoId));
  }

  private Mono<TodoRequest> validateTodoRequestContent(Mono<TodoRequest> todoResourceMono) {
    return todoResourceMono
        .filter(todoResource -> StringUtils.hasText(todoResource.getTask()))
        .switchIfEmpty(
            Mono.error(
                new TodoRuntimeException(
                    HttpStatus.BAD_REQUEST, String.format("Todo content cannot be empty"))));
  }
}
