package com.spring.webflux.todo.router;

import com.spring.webflux.todo.dto.StandardTags;
import com.spring.webflux.todo.dto.request.TodoRequest;
import com.spring.webflux.todo.dto.response.TodoResponse;
import com.spring.webflux.todo.exception.InvalidSectionRuntimeException;
import com.spring.webflux.todo.exception.InvalidTodoException;
import com.spring.webflux.todo.exception.TodoRuntimeException;
import com.spring.webflux.todo.mapper.TodoMapper;
import com.spring.webflux.todo.repository.SectionRepository;
import com.spring.webflux.todo.repository.TodoRepository;
import java.net.URI;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequiredArgsConstructor
@Slf4j
public class TodoRouteHandler {
  private final TodoRepository todoRepository;
  private final SectionRepository sectionRepository;

  public Mono<ServerResponse> getAllTodo(String sectionId, ServerRequest request) {
    return getAuthenticatedPrincipal(request)
        .flatMap(
            oAuth2AuthenticatedPrincipal ->
                isSectionValid(getAuthenticateUserEmail(oAuth2AuthenticatedPrincipal), sectionId))
        .flatMap(
            unused ->
                ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(todoRepository.findAllBySectionId(sectionId), TodoResponse.class));
  }

  public Mono<ServerResponse> getTodoById(String sectionId, String todoId, ServerRequest request) {
    return getAuthenticatedPrincipal(request)
        .flatMap(
            oAuth2AuthenticatedPrincipal ->
                isSectionValid(getAuthenticateUserEmail(oAuth2AuthenticatedPrincipal), sectionId))
        .flatMap(unused -> todoRepository.findById(todoId))
        .map(TodoMapper.INSTANCE::entityToResponse)
        .flatMap(todoResponse -> ServerResponse.ok().body(BodyInserters.fromValue(todoResponse)));
  }

  public Mono<ServerResponse> createTodo(String sectionId, ServerRequest request) {
    return getAuthenticatedPrincipal(request)
        .flatMap(
            oAuth2AuthenticatedPrincipal ->
                validateAndGetTodoRequest(
                    getAuthenticateUserEmail(oAuth2AuthenticatedPrincipal), sectionId, request))
        .flatMap(
            todoRequest ->
                todoRepository.save(TodoMapper.INSTANCE.requestToEntity(sectionId, todoRequest)))
        .flatMap(
            todo ->
                ServerResponse.created(URI.create("/todo/" + todo.getId()))
                    .body(BodyInserters.fromValue(todo)));
  }

  public Mono<ServerResponse> updateTodo(String sectionId, String id, ServerRequest request) {
    return getAuthenticatedPrincipal(request)
        .flatMap(
            oAuth2AuthenticatedPrincipal ->
                validateAndGetTodoRequest(
                    getAuthenticateUserEmail(oAuth2AuthenticatedPrincipal), sectionId, request))
        .flatMap(
            todoRequest ->
                todoRepository
                    .existsById(id)
                    .filter(Boolean::valueOf)
                    .switchIfEmpty(Mono.error(() -> new InvalidTodoException(id)))
                    .thenReturn(todoRequest))
        .flatMap(
            todoRequest ->
                todoRepository.save(TodoMapper.INSTANCE.requestToEntity(sectionId, todoRequest)))
        .map(TodoMapper.INSTANCE::entityToResponse)
        .flatMap(
            todoResponse ->
                ServerResponse.ok()
                    .location(URI.create("/todo/" + id))
                    .body(BodyInserters.fromValue(todoResponse)));
  }

  public Mono<ServerResponse> deleteTodo(String sectionId, String id, ServerRequest request) {
    return getAuthenticatedPrincipal(request)
        .flatMap(
            oAuth2AuthenticatedPrincipal ->
                isSectionValid(getAuthenticateUserEmail(oAuth2AuthenticatedPrincipal), sectionId))
        .flatMap(principal -> todoRepository.deleteById(id))
        .then(ServerResponse.ok().build());
  }

  public Mono<ServerResponse> getStandardTags(ServerRequest request) {
    return ServerResponse.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(Flux.fromStream(Arrays.stream(StandardTags.values())), StandardTags.class);
  }

  private Mono<TodoRequest> validateAndGetTodoRequest(
      String userId, String sectionId, ServerRequest request) {
    return isSectionValid(userId, sectionId)
        .flatMap(
            unused ->
                request
                    .bodyToMono(TodoRequest.class)
                    .filter(todoResource -> StringUtils.hasText(todoResource.getTask()))
                    .switchIfEmpty(
                        Mono.error(
                            () ->
                                new TodoRuntimeException(
                                    HttpStatus.BAD_REQUEST,
                                    String.format("Todo content cannot be empty")))));
  }

  private Mono<Boolean> isSectionValid(String userId, String sectionId) {
    return sectionRepository
        .existsByUserIdAndId(userId, sectionId)
        .filter(Boolean::booleanValue)
        .switchIfEmpty(Mono.error(() -> new InvalidSectionRuntimeException(sectionId)));
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
