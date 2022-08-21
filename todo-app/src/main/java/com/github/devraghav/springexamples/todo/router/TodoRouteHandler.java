package com.github.devraghav.springexamples.todo.router;

import com.github.devraghav.springexamples.todo.dto.StandardTags;
import com.github.devraghav.springexamples.todo.dto.request.TodoRequest;
import com.github.devraghav.springexamples.todo.dto.response.TodoResponse;
import com.github.devraghav.springexamples.todo.exception.InvalidSectionRuntimeException;
import com.github.devraghav.springexamples.todo.exception.InvalidTodoException;
import com.github.devraghav.springexamples.todo.exception.TodoRuntimeException;
import com.github.devraghav.springexamples.todo.mapper.TodoMapper;
import com.github.devraghav.springexamples.todo.repository.SectionRepository;
import com.github.devraghav.springexamples.todo.repository.TodoRepository;
import com.github.devraghav.springexamples.todo.security.SecurityUtils;
import io.swagger.v3.oas.annotations.Parameter;
import java.net.URI;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

  public Mono<ServerResponse> disable(
      @Parameter(hidden = true) Mono<OAuth2AuthenticatedPrincipal> oAuth2AuthenticatedPrincipalMono,
      String sectionId,
      String id) {
    return validateSection(oAuth2AuthenticatedPrincipalMono, sectionId)
        .flatMap(unused -> todoRepository.existsById(id).filter(Boolean::booleanValue))
        .switchIfEmpty(Mono.error(() -> new InvalidTodoException(id)))
        .flatMap(todoId -> todoRepository.disableTodo(id))
        .then(ServerResponse.ok().build());
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
                () -> new TodoRuntimeException(String.format("Todo content cannot be empty"))));
  }

  private Mono<Boolean> validateSection(
      Mono<OAuth2AuthenticatedPrincipal> oAuth2AuthenticatedPrincipalMono, String sectionId) {
    return oAuth2AuthenticatedPrincipalMono
        .map(SecurityUtils::getUserId)
        .flatMap(
            userId ->
                sectionRepository
                    .existsByUserIdAndId(userId, sectionId)
                    .filter(Boolean::booleanValue))
        .switchIfEmpty(Mono.error(() -> new InvalidSectionRuntimeException(sectionId)));
  }
}
