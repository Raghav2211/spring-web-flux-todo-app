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
import io.swagger.v3.oas.annotations.Parameter;
import java.net.URI;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
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

  public Mono<ServerResponse> getAllTodo(
      @Parameter(hidden = true) Mono<OAuth2AuthenticatedPrincipal> oAuth2AuthenticatedPrincipalMono,
      String sectionId) {
    // spotless:off
    return validateSection(oAuth2AuthenticatedPrincipalMono, sectionId)
        .flatMap(unused ->
                ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(todoRepository.findAllBySectionId(sectionId), TodoResponse.class));
    // spotless:on
  }

  public Mono<ServerResponse> getTodoById(
      @Parameter(hidden = true) Mono<OAuth2AuthenticatedPrincipal> oAuth2AuthenticatedPrincipalMono,
      String sectionId,
      String id) {
    return validateSection(oAuth2AuthenticatedPrincipalMono, sectionId)
        .flatMap(unused -> todoRepository.findById(id))
        .map(TodoMapper.INSTANCE::entityToResponse)
        .flatMap(todoResponse -> ServerResponse.ok().body(BodyInserters.fromValue(todoResponse)));
  }

  public Mono<ServerResponse> createTodo(
      @Parameter(hidden = true) Mono<OAuth2AuthenticatedPrincipal> oAuth2AuthenticatedPrincipalMono,
      String sectionId,
      ServerRequest request) {

    return validateSection(oAuth2AuthenticatedPrincipalMono, sectionId)
        .thenReturn(request)
        .flatMap(this::validateAndGetTodoRequest)
        .flatMap(
            todoRequest ->
                todoRepository.save(TodoMapper.INSTANCE.requestToEntity(sectionId, todoRequest)))
        .flatMap(
            todo ->
                ServerResponse.created(URI.create("/todo/" + todo.getId()))
                    .body(BodyInserters.fromValue(todo)));
  }

  public Mono<ServerResponse> updateTodo(
      @Parameter(hidden = true) Mono<OAuth2AuthenticatedPrincipal> oAuth2AuthenticatedPrincipalMono,
      String sectionId,
      String id,
      ServerRequest request) {
    return validateSection(oAuth2AuthenticatedPrincipalMono, sectionId)
        .thenReturn(request)
        .flatMap(this::validateAndGetTodoRequest)
        .flatMap(
            todoRequest ->
                todoRepository
                    .existsById(id)
                    .filter(Boolean::valueOf)
                    .switchIfEmpty(Mono.error(() -> new InvalidTodoException(id)))
                    .thenReturn(TodoMapper.INSTANCE.requestToEntity(sectionId, todoRequest)))
        .flatMap(todo -> todoRepository.save(todo))
        .map(TodoMapper.INSTANCE::entityToResponse)
        .flatMap(
            todoResponse ->
                ServerResponse.ok()
                    .location(URI.create("/todo/" + id))
                    .body(BodyInserters.fromValue(todoResponse)));
  }

  public Mono<ServerResponse> deleteTodo(
      @Parameter(hidden = true) Mono<OAuth2AuthenticatedPrincipal> oAuth2AuthenticatedPrincipalMono,
      String sectionId,
      String id) {
    return validateSection(oAuth2AuthenticatedPrincipalMono, sectionId)
        .flatMap(principal -> todoRepository.deleteById(id))
        .then(ServerResponse.ok().build());
  }

  public Mono<ServerResponse> getStandardTags() {
    return ServerResponse.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(Flux.fromStream(Arrays.stream(StandardTags.values())), StandardTags.class);
  }

  private Mono<TodoRequest> validateAndGetTodoRequest(ServerRequest request) {
    return request
        .bodyToMono(TodoRequest.class)
        .filter(todoResource -> StringUtils.hasText(todoResource.getTask()))
        .switchIfEmpty(
            Mono.error(
                () ->
                    new TodoRuntimeException(
                        HttpStatus.BAD_REQUEST, String.format("Todo content cannot be empty"))));
  }

  private Mono<Boolean> validateSection(
      Mono<OAuth2AuthenticatedPrincipal> oAuth2AuthenticatedPrincipalMono, String sectionId) {
    return oAuth2AuthenticatedPrincipalMono
        .flatMap(
            oAuth2AuthenticatedPrincipal ->
                sectionRepository
                    .existsByUserIdAndId(
                        getAuthenticateUserEmail(oAuth2AuthenticatedPrincipal), sectionId)
                    .filter(Boolean::booleanValue))
        .switchIfEmpty(Mono.error(() -> new InvalidSectionRuntimeException(sectionId)));
  }

  private String getAuthenticateUserEmail(
      OAuth2AuthenticatedPrincipal oAuth2AuthenticatedPrincipal) {
    return String.valueOf(oAuth2AuthenticatedPrincipal.getAttributes().get("email"));
  }
}
