package com.spring.webflux.todo.api;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.webflux.todo.dto.TodoRequest;
import com.spring.webflux.todo.entity.Todo;
import com.spring.webflux.todo.repository.TodoRepository;
import java.io.File;
import java.io.IOException;
import java.util.List;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public abstract class AbstractTodoTest {
  private WebTestClient webclient;

  protected abstract String apiRootPath();

  @MockBean private TodoRepository todoRepository;
  @Autowired private ApplicationContext context;

  private static Todo todoResponse;
  private static TodoRequest todoRequest;
  private static TodoRequest todoInvalidRequest;

  @BeforeAll
  @SneakyThrows({JsonParseException.class, JsonMappingException.class, IOException.class})
  public static void beforeClass() {
    File resposeFile = new File("src/test/resources/data/todoResponse.json");
    todoResponse = new ObjectMapper().readValue(resposeFile, Todo.class);
    File requestFile = new File("src/test/resources/data/todoRequest.json");
    todoRequest = new ObjectMapper().readValue(requestFile, TodoRequest.class);
    File invalidRequestFile = new File("src/test/resources/data/todoInvalidRequest.json");
    todoInvalidRequest = new ObjectMapper().readValue(invalidRequestFile, TodoRequest.class);
  }

  @BeforeEach
  public void setUp() {
    webclient = WebTestClient.bindToApplicationContext(context).build();
  }

  @SneakyThrows
  @Test
  @WithMockUser
  public void testGetTodoById() {
    Mockito.when(todoRepository.findById(1)).thenReturn(Mono.just(todoResponse));
    webclient
        .get()
        .uri(apiRootPath() + "/1")
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.task")
        .isEqualTo(todoResponse.getTask());
    Mockito.verify(todoRepository).findById(1);
    Mockito.verifyNoMoreInteractions(todoRepository);
  }

  @SneakyThrows
  @Test
  @WithMockUser
  public void testGetTodoByIdNotFound() {
    Mockito.when(todoRepository.findById(1)).thenReturn(Mono.empty());
    webclient
        .get()
        .uri(apiRootPath() + "/1")
        .exchange()
        .expectStatus()
        .isNotFound()
        .expectHeader()
        .valueEquals("id", 1);
    Mockito.verify(todoRepository).findById(1);
    Mockito.verifyNoMoreInteractions(todoRepository);
  }

  @SneakyThrows
  @Test
  @WithMockUser
  public void testGetAllTodo() {
    Mockito.when(todoRepository.findAll()).thenReturn(Flux.fromIterable(List.of(todoResponse)));

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
  @WithMockUser
  public void testCreateTodo() {
    Todo returnTodo = new Todo();
    returnTodo.setId("1");
    returnTodo.setTask(todoResponse.getTask());
    Mockito.when(todoRepository.save(Mockito.any(Todo.class))).thenReturn(Mono.just(returnTodo));
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
  @WithMockUser
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
  @WithMockUser
  public void testUpdateTodo() {
    Todo returnTodo = new Todo();
    returnTodo.setId("1");
    returnTodo.setTask("DB TODO");
    Mockito.when(todoRepository.findById(1)).thenReturn(Mono.just(returnTodo));
    Mockito.when(todoRepository.save(Mockito.any(Todo.class))).thenReturn(Mono.just(returnTodo));

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
        .jsonPath("$.task")
        .isEqualTo(todoRequest.getTask());

    Mockito.verify(todoRepository).findById(1);
    Mockito.verify(todoRepository).save(Mockito.any(Todo.class));
    Mockito.verifyNoMoreInteractions(todoRepository);
  }

  @SneakyThrows
  @Test
  @WithMockUser
  public void testUpdateNonExistTodo() {
    Mockito.when(todoRepository.findById(1)).thenReturn(Mono.empty());
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

    Mockito.verify(todoRepository).findById(1);
    Mockito.verifyNoMoreInteractions(todoRepository);
  }

  @SneakyThrows
  @Test
  @WithMockUser
  public void testDeleteTodo() {
    Mockito.when(todoRepository.deleteById(1)).thenReturn(Mono.empty());
    webclient.delete().uri(apiRootPath() + "/1").exchange().expectStatus().isOk();
    Mockito.verify(todoRepository).deleteById(1);
    Mockito.verifyNoMoreInteractions(todoRepository);
  }

  @SneakyThrows
  @Test
  @WithMockUser
  public void testDeleteTodoWithNegativeId() {
    webclient
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
