package com.github.devraghav.springexamples.todo.api;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.devraghav.springexamples.todo.dto.request.TodoRequest;
import com.github.devraghav.springexamples.todo.dto.response.TodoResponse;
import com.github.devraghav.springexamples.todo.entity.Todo;
import com.github.devraghav.springexamples.todo.repository.TodoRepository;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

@Disabled
public abstract class AbstractTodoTest {
  private static OAuth2AuthenticatedPrincipal MOCK_AUTHENTICATION_PRINCIPAL =
      new OAuth2AuthenticatedPrincipal() {
        @Override
        public Map<String, Object> getAttributes() {
          return Map.of("email", "mock@mock.com");
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
          return List.of(new SimpleGrantedAuthority("SCOPE_USER"));
        }

        @Override
        public String getName() {
          return null;
        }
      };

  private static BearerTokenAuthentication MOCK_BEARER_TOKEN =
      new BearerTokenAuthentication(
          MOCK_AUTHENTICATION_PRINCIPAL,
          new OAuth2AccessToken(
              OAuth2AccessToken.TokenType.BEARER, "mockToken", Instant.MIN, Instant.MAX),
          MOCK_AUTHENTICATION_PRINCIPAL.getAuthorities());

  private WebTestClient webclient;

  protected abstract String apiRootPath();

  @MockBean private TodoRepository todoRepository;
  @Autowired private ApplicationContext context;

  private static TodoResponse todoResponse;
  private static TodoRequest todoRequest;
  private static TodoRequest todoInvalidRequest;

  @BeforeAll
  @SneakyThrows({JsonParseException.class, JsonMappingException.class, IOException.class})
  public static void beforeClass() {
    File resposeFile = new File("src/test/resources/data/todoResponse.json");
    todoResponse = new ObjectMapper().readValue(resposeFile, TodoResponse.class);
    File requestFile = new File("src/test/resources/data/request.json");
    todoRequest = new ObjectMapper().readValue(requestFile, TodoRequest.class);
    File invalidRequestFile = new File("src/test/resources/data/invalidRequest.json");
    todoInvalidRequest = new ObjectMapper().readValue(invalidRequestFile, TodoRequest.class);
  }

  @BeforeEach
  public void setUp() {

    webclient =
        WebTestClient.bindToApplicationContext(context)
            .apply(SecurityMockServerConfigurers.springSecurity())
            .configureClient()
            .build()
            .mutateWith(
                SecurityMockServerConfigurers.mockOpaqueToken()
                    .principal(MOCK_AUTHENTICATION_PRINCIPAL))
            .mutateWith(SecurityMockServerConfigurers.mockAuthentication(MOCK_BEARER_TOKEN));
    ;
  }

  @SneakyThrows
  @Test
  public void testGetTodoById() {
    //    Mockito.when(todoRepository.findById(1)).thenReturn(Mono.just(todoResponse));
    webclient
        .mutateWith(SecurityMockServerConfigurers.mockOpaqueToken())
        .get()
        .uri(apiRootPath() + "/1")
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
    //        .jsonPath("$.task")
    //        .isEqualTo(todoResponse.getTask())
    ;
    //    Mockito.verify(todoRepository).findById(1);
    Mockito.verifyNoMoreInteractions(todoRepository);
  }

  @SneakyThrows
  @Test
  public void testGetTodoByIdNotFound() {
    //    Mockito.when(todoRepository.findById(1)).thenReturn(Mono.empty());
    webclient
        .get()
        .uri(apiRootPath() + "/1")
        .exchange()
        .expectStatus()
        .isNotFound()
        .expectHeader()
        .valueEquals("id", 1);
    //    Mockito.verify(todoRepository).findById(1);
    Mockito.verifyNoMoreInteractions(todoRepository);
  }

  @SneakyThrows
  @Test
  public void testGetAllTodo() {
    //
    // Mockito.when(todoRepository.findAll()).thenReturn(Flux.fromIterable(List.of(todoResponse)));

    webclient
        .get()
        .uri(apiRootPath())
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.[0].id")
        .isEqualTo(1);
    Mockito.verify(todoRepository).findAll();
    Mockito.verifyNoMoreInteractions(todoRepository);
  }

  @SneakyThrows
  @Test
  public void testCreateTodo() {
    //    var returnTodo = new TodoResponse();
    //    returnTodo.setId("1");
    //    Mockito.when(todoRepository.save(Mockito.any(UserTodoList.class)))
    //        .thenReturn(Mono.just(returnTodo));
    webclient
        .post()
        .uri(apiRootPath())
        .body(BodyInserters.fromValue(todoRequest))
        .headers(httpHeaders -> httpHeaders.setContentType(MediaType.APPLICATION_JSON))
        .exchange()
        .expectStatus()
        .isCreated()
        .expectBody()
        .jsonPath("$.id")
        .isEqualTo(1);
    Mockito.verify(todoRepository).save(Mockito.any(Todo.class));
    Mockito.verifyNoMoreInteractions(todoRepository);
  }

  @SneakyThrows
  @Test
  @Disabled
  public void testCreateWithInvalidTodo() {
    webclient
        .post()
        .uri(apiRootPath())
        .body(BodyInserters.fromValue(todoInvalidRequest))
        .headers(httpHeaders -> httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON)))
        .exchange()
        .expectStatus()
        .isBadRequest();
    Mockito.verifyNoInteractions(todoRepository);
  }

  @SneakyThrows
  @Test
  public void testUpdateTodo() {
    //    UserTodoList returnTodo = new UserTodoList();
    //    returnTodo.setId("1");
    //    returnTodo.setTask("DB TODO");
    //    Mockito.when(todoRepository.findById(1)).thenReturn(Mono.just(returnTodo));
    Mockito.when(todoRepository.save(Mockito.any(Todo.class)))
        .thenReturn(Mono.just(Mockito.any(Todo.class)));

    webclient
        .put()
        .uri(apiRootPath() + "/1")
        .body(BodyInserters.fromValue(todoRequest))
        .headers(httpHeaders -> httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON)))
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.id")
        .isEqualTo(1)
    //        .jsonPath("$.task")
    //        .isEqualTo(todoRequest.getTask())
    ;

    //    Mockito.verify(todoRepository).findById(1);
    Mockito.verify(todoRepository).save(Mockito.any(Todo.class));
    Mockito.verifyNoMoreInteractions(todoRepository);
  }

  @SneakyThrows
  @Test
  public void testUpdateNonExistTodo() {
    //    Mockito.when(todoRepository.findById(1)).thenReturn(Mono.empty());
    webclient
        .put()
        .uri(apiRootPath() + "/1")
        .body(BodyInserters.fromValue(todoRequest))
        .headers(httpHeaders -> httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON)))
        .exchange()
        .expectStatus()
        .isNotFound()
        .expectHeader()
        .valueEquals("id", 1);

    //    Mockito.verify(todoRepository).findById(1);
    Mockito.verifyNoMoreInteractions(todoRepository);
  }

  @SneakyThrows
  @Test
  public void testDeleteTodo() {
    Mockito.when(todoRepository.deleteById("1")).thenReturn(Mono.empty());
    webclient.delete().uri(apiRootPath() + "/1").exchange().expectStatus().isOk();
    Mockito.verify(todoRepository).deleteById("1");
    Mockito.verifyNoMoreInteractions(todoRepository);
  }

  @SneakyThrows
  @Test
  public void testDeleteTodoWithNegativeId() {
    webclient
        .mutateWith(SecurityMockServerConfigurers.mockOpaqueToken())
        .delete()
        .uri(apiRootPath() + "/-1")
        .exchange()
        .expectStatus()
        .isBadRequest()
        .expectHeader()
        .valueEquals("id", "-1");
    Mockito.verifyNoInteractions(todoRepository);
  }
}
