package com.spring.webflux.todo.controller;

import com.spring.webflux.todo.dto.TodoResource;
import com.spring.webflux.todo.entity.Todo;
import com.spring.webflux.todo.exception.TodoException;
import com.spring.webflux.todo.service.ITodoService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = {"/api/v1/todo"})
public class TodoController {

  private ITodoService todoService;

  public TodoController(ITodoService todoService) {
    this.todoService = todoService;
  }

  @ApiOperation(
      value = "View Todo by provided id",
      response = Todo.class,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "Retrieved todo successfully"),
        @ApiResponse(code = 400, message = "Todo [id] not found", response = TodoException.class),
      })
  @ApiResponse(code = 401, message = "Unauthorized")
  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Mono<Todo>> getTodoById(@PathVariable Long id) {
    return new ResponseEntity<Mono<Todo>>(todoService.findById(id), HttpStatus.OK);
  }

  @ApiOperation(value = "Create Todo", response = Todo.class)
  @ApiResponses(
      value = {
        @ApiResponse(code = 201, message = "Todo successfully created"),
        @ApiResponse(code = 401, message = "Unauthorized")
      })
  @PostMapping(
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Mono<Todo>> createTodo(@RequestBody Mono<TodoResource> requestTodo) {
    return new ResponseEntity<Mono<Todo>>(todoService.create(requestTodo), HttpStatus.CREATED);
  }

  @ApiOperation(value = "View all Todos", response = List.class)
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "Retrieved all todos",
            response = Todo.class,
            responseContainer = "List"),
        @ApiResponse(code = 401, message = "Unauthorized")
      })
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Flux<Todo> getAllTodo() {
    return todoService.findAll();
  }

  @ApiOperation(value = "Update Todo", response = Todo.class)
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "Todo successfully updated", response = Todo.class),
        @ApiResponse(code = 400, message = "Todo[id] not found", response = TodoException.class),
        @ApiResponse(code = 401, message = "Unauthorized")
      })
  @PutMapping(
      value = "/{id}",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Mono<Todo>> updateTodo(
      @RequestBody Mono<TodoResource> todo, @PathVariable Long id) {
    return new ResponseEntity<Mono<Todo>>(todoService.update(todo, id), HttpStatus.OK);
  }

  @ApiOperation(value = "Delete Todo by provide id")
  @ApiResponses(
      value = {
        @ApiResponse(code = 204, message = "Todo successfully deleted", response = Void.class),
        @ApiResponse(
            code = 400,
            message = "No Todo with id [id] exists!",
            response = TodoException.class),
        @ApiResponse(code = 401, message = "Unauthorized"),
        @ApiResponse(
            code = 500,
            message = "No Todo with id exists!",
            response = TodoException.class)
      })
  @DeleteMapping(value = "/{id}")
  public ResponseEntity<Mono<Void>> deleteTodo(@PathVariable Long id) {
    var httpHeader = new HttpHeaders();
    httpHeader.add("id", String.valueOf(id));
    return id == null || id < 0
        ? new ResponseEntity<Mono<Void>>(httpHeader, HttpStatus.BAD_REQUEST)
        : new ResponseEntity<Mono<Void>>(todoService.delete(id), HttpStatus.OK);
  }
}
