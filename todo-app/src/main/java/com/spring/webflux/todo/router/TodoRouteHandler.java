package com.spring.webflux.todo.router;

import com.spring.webflux.todo.dto.StandardTags;
import com.spring.webflux.todo.dto.TodoRequest;
import com.spring.webflux.todo.entity.Todo;
import com.spring.webflux.todo.exception.InvalidTodoException;
import com.spring.webflux.todo.exception.TodoRuntimeException;
import com.spring.webflux.todo.repository.TodoRepository;
import com.spring.webflux.todo.service.ITodoService;
import java.net.URI;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
@Slf4j
public class TodoRouteHandler {
  private ITodoService todoService;
  private TodoRepository todoRepository;

  public Mono<ServerResponse> getAllTodo(ServerRequest request) {
    return ServerResponse.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(todoService.findAll(), Todo.class);
  }

  public Mono<ServerResponse> getTodoById(ServerRequest request) {
    return todoService
        .findById(Integer.valueOf(request.pathVariable("id")))
        .flatMap((todo) -> ServerResponse.ok().body(BodyInserters.fromValue(todo)));
  }

  public Mono<ServerResponse> createTodo(ServerRequest request) {

    return Mono.zip(getAuthenticatedPrincipal(request), validateAndGetTodoResource(request))
        .flatMap(tuple -> todoRepository.save(mapToTodo(tuple.getT2())))
        .flatMap(
            todo ->
                ServerResponse.created(URI.create("/todo/" + todo.getId()))
                    .body(BodyInserters.fromValue(todo)));
  }

  public Mono<ServerResponse> updateTodo(ServerRequest request) {
    var id = Integer.valueOf(request.pathVariable("id"));
    return validateAndGetTodoResource(request)
        .flatMap(
            todoResource ->
                todoService
                    .findById(id)
                    .doOnNext(todo -> updateExistingTodo(todo, todoResource))
                    .flatMap(todo -> todoRepository.save(todo)))
        .flatMap(
            todo ->
                ServerResponse.ok()
                    .location(URI.create("/todo/" + todo.getId()))
                    .body(BodyInserters.fromValue(todo)));
  }

  public Mono<ServerResponse> deleteTodo(ServerRequest request) {
    var id = Integer.valueOf(request.pathVariable("id"));
    return getAuthenticatedPrincipal(request)
        .flatMap(principal -> todoService.delete(getAuthenticateUserEmail(principal), id))
        .flatMap(unused -> ServerResponse.ok().build())
        .onErrorResume(
            EmptyResultDataAccessException.class,
            unused -> Mono.error(new InvalidTodoException(id)));
  }

  public Mono<ServerResponse> getStandardTags(ServerRequest request) {
    return ServerResponse.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(Flux.fromStream(Arrays.stream(StandardTags.values())), StandardTags.class);
  }

  public Todo mapToTodo(TodoRequest todoResource) {
    Todo todo = new Todo();
    todo.setTask(todoResource.getTask());
    return todo;
  }

  private Mono<TodoRequest> validateAndGetTodoResource(ServerRequest request) {
    return request
        .bodyToMono(TodoRequest.class)
        .filter(todoResource -> StringUtils.hasText(todoResource.getTask()))
        .switchIfEmpty(
            Mono.error(
                new TodoRuntimeException(
                    HttpStatus.BAD_REQUEST, String.format("Todo content cannot be empty"))));
  }

  private void updateExistingTodo(Todo todo, TodoRequest todoResource) {
    todo.setTask(todoResource.getTask());
  }

  private Mono<OAuth2AuthenticatedPrincipal> getAuthenticatedPrincipal(ServerRequest request) {
    return request
        .principal()
        .filter(
            principal ->
                ((BearerTokenAuthentication) principal).getPrincipal()
                    instanceof OAuth2AuthenticatedPrincipal)
        .cast(BearerTokenAuthentication.class)
        .map(bearerTokenAuthentication -> bearerTokenAuthentication.getPrincipal())
        .filter(principal -> principal instanceof OAuth2AuthenticatedPrincipal)
        .cast(OAuth2AuthenticatedPrincipal.class);
  }

  private String getAuthenticateUserEmail(
      OAuth2AuthenticatedPrincipal oAuth2AuthenticatedPrincipal) {
    return String.valueOf(oAuth2AuthenticatedPrincipal.getAttributes().get("email"));
  }
}
