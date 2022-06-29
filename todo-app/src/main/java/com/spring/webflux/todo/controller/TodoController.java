package com.spring.webflux.todo.controller;

import com.spring.webflux.todo.dto.TodoRequest;
import com.spring.webflux.todo.entity.Todo;
import com.spring.webflux.todo.service.ITodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = {"/api/v1/todo"})
public class TodoController {
  public static final String REQUEST_HEADER_ID = "id";
  private ITodoService todoService;

  public TodoController(ITodoService todoService) {
    this.todoService = todoService;
  }

  @Operation(summary = "Get a todo by its id", operationId = "getTodoById")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Retrieved todo successfully",
            content = @Content(schema = @Schema(implementation = Todo.class))),
        @ApiResponse(
            responseCode = "404",
            description = "Todo not found",
            headers = {@Header(name = REQUEST_HEADER_ID)},
            content = {@Content(schema = @Schema)}),
        @ApiResponse(
            responseCode = "400",
            description = "Bad Request",
            headers = {@Header(name = REQUEST_HEADER_ID)},
            content = {@Content(schema = @Schema)}),
        @ApiResponse(
            responseCode = "401",
            description = "Unauthorized",
            content = {@Content(schema = @Schema)})
      })
  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Mono<Todo>> getTodoById(@PathVariable Integer id) {
    return new ResponseEntity<Mono<Todo>>(todoService.findById(id), HttpStatus.OK);
  }

  @Operation(summary = "Persist todo", operationId = "createTodo")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "201",
            description = "Todo successfully created",
            content = @Content(schema = @Schema(implementation = Todo.class))),
        @ApiResponse(
            responseCode = "400",
            description = "Bad Request",
            content = {@Content(schema = @Schema)}),
        @ApiResponse(
            responseCode = "401",
            description = "Unauthorized",
            content = {@Content(schema = @Schema)})
      })
  @PostMapping(
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Mono<Todo>> createTodo(@RequestBody Mono<TodoRequest> requestTodo) {
    return new ResponseEntity<Mono<Todo>>(todoService.create(requestTodo), HttpStatus.CREATED);
  }

  @Operation(summary = "Get all todos", operationId = "getAllTodo")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Retrieved all todos",
            content = @Content(schema = @Schema(implementation = Todo.class))),
        @ApiResponse(
            responseCode = "401",
            description = "Unauthorized",
            content = {@Content(schema = @Schema)})
      })
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Flux<Todo> getAllTodo() {
    return todoService.findAll();
  }

  @Operation(summary = "Update todo", operationId = "updateTodo")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Todo successfully updated",
            content = @Content(schema = @Schema(implementation = Todo.class))),
        @ApiResponse(
            responseCode = "404",
            description = "Todo not found",
            headers = {@Header(name = REQUEST_HEADER_ID)},
            content = {@Content(schema = @Schema)}),
        @ApiResponse(
            responseCode = "400",
            description = "Bad Request",
            content = {@Content(schema = @Schema)}),
        @ApiResponse(
            responseCode = "401",
            description = "Unauthorized",
            content = {@Content(schema = @Schema)})
      })
  @PutMapping(
      value = "/{id}",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Mono<Todo>> updateTodo(
      @RequestBody Mono<TodoRequest> todo, @PathVariable Integer id) {
    return new ResponseEntity<Mono<Todo>>(todoService.update(todo, id), HttpStatus.OK);
  }

  @Operation(summary = "Delete todo by id", operationId = "deleteTodo")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "204",
            description = "Todo successfully deleted",
            content = {@Content(schema = @Schema)}),
        @ApiResponse(
            responseCode = "404",
            description = "Todo not found",
            headers = {@Header(name = "id")},
            content = {@Content(schema = @Schema)}),
        @ApiResponse(
            responseCode = "400",
            description = "Bad Request",
            content = {@Content(schema = @Schema)}),
        @ApiResponse(
            responseCode = "401",
            description = "Unauthorized",
            content = {@Content(schema = @Schema)})
      })
  @DeleteMapping(value = "/{id}")
  public ResponseEntity<Mono<Void>> deleteTodo(@PathVariable Integer id) {
    var httpHeader = new HttpHeaders();
    httpHeader.add("id", String.valueOf(id));
    return id == null || id < 0
        ? new ResponseEntity<Mono<Void>>(httpHeader, HttpStatus.BAD_REQUEST)
        : new ResponseEntity<Mono<Void>>(todoService.delete(id), HttpStatus.OK);
  }
}