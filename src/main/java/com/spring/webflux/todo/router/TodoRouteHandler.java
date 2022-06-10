package com.spring.webflux.todo.router;

import com.spring.webflux.todo.dto.TodoResource;
import com.spring.webflux.todo.entity.Todo;
import com.spring.webflux.todo.exception.TodoNotFoundException;
import com.spring.webflux.todo.repository.TodoRepository;
import com.spring.webflux.todo.service.ITodoService;
import java.net.URI;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
@AllArgsConstructor
public class TodoRouteHandler {
  private ITodoService todoService;
  private TodoRepository todoRepository;

  public Mono<ServerResponse> getAllTodo(ServerRequest request) {
    return ServerResponse.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(todoService.findAll(), Todo.class);
  }

  public Mono<ServerResponse> getTodoById(ServerRequest request) {
    var id = Long.valueOf(request.pathVariable("id"));
    return todoService
        .findById(id)
        .flatMap((todo) -> ServerResponse.ok().body(BodyInserters.fromValue(todo)))
        .onErrorResume(
            TodoNotFoundException.class,
            unused ->
                ServerResponse.notFound()
                    .headers(httpHeaders -> httpHeaders.add("id", String.valueOf(id)))
                    .build());
  }

  public Mono<ServerResponse> createTodo(ServerRequest request) {
    return request
        .bodyToMono(TodoResource.class)
        .filter(todoResource -> StringUtils.hasText(todoResource.getContent()))
        .switchIfEmpty(
            Mono.error(new IllegalArgumentException(String.format("Todo content cannot be empty"))))
        .map(this::mapToTodo)
        .flatMap(
            todo ->
                Mono.fromCallable(() -> todoRepository.save(todo))
                    .subscribeOn(Schedulers.boundedElastic()))
        .flatMap(
            todo ->
                ServerResponse.created(URI.create("/todo/" + todo.getId()))
                    .body(BodyInserters.fromValue(todo)));
  }

  public Mono<ServerResponse> updateTodo(ServerRequest request) {
    var id = Long.valueOf(request.pathVariable("id"));
    return request
        .bodyToMono(TodoResource.class)
        .filter(todoResource -> StringUtils.hasText(todoResource.getContent()))
        .switchIfEmpty(
            Mono.error(new IllegalArgumentException(String.format("Todo content cannot be empty"))))
        .flatMap(
            todoResource ->
                todoService
                    .findById(id)
                    .doOnNext(todo -> updateExistingTodo(todo, todoResource))
                    .map(todo -> todoRepository.save(todo))
                    .subscribeOn(Schedulers.boundedElastic()))
        .flatMap(
            todo ->
                ServerResponse.ok()
                    .location(URI.create("/todo/" + todo.getId()))
                    .body(BodyInserters.fromValue(todo)));
  }

  public Mono<ServerResponse> deleteTodo(ServerRequest request) {
    var id = Long.valueOf(request.pathVariable("id"));
    return ServerResponse.noContent().build(todoService.delete(id));
  }

  public Todo mapToTodo(TodoResource todoResource) {
    Todo todo = new Todo();
    todo.setContent(todoResource.getContent());
    todo.setIsComplete(todoResource.getIsComplete());
    return todo;
  }

  private void updateExistingTodo(Todo todo, TodoResource todoResource) {
    todo.setIsComplete(todoResource.getIsComplete());
    todo.setContent(todoResource.getContent());
  }
}
