package com.spring.webflux.todo.controller;

import com.spring.webflux.todo.dto.StandardTags;
import com.spring.webflux.todo.dto.request.Request;
import com.spring.webflux.todo.entity.UserTodoList;
import com.spring.webflux.todo.service.ITodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.Arrays;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = {"/api/v1/{sectionId}/todo"})
@SecurityRequirement(name = "bearerAuth")
public class TodoController {
  public static final String REQUEST_HEADER_ID = "id";
  private ITodoService todoService;

  public TodoController(ITodoService todoService) {
    this.todoService = todoService;
  }

  @Operation(summary = "Get a todo by its id", operationId = "getTodoById")
  @Parameter(in = ParameterIn.PATH, name = "sectionId", schema = @Schema(type = "string"))
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Retrieved todo successfully",
            content = @Content(schema = @Schema(implementation = UserTodoList.class))),
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
  public ResponseEntity<Mono<UserTodoList>> getTodoById(@PathVariable Integer id) {
    return new ResponseEntity<Mono<UserTodoList>>(todoService.findById(id), HttpStatus.OK);
  }

  @Operation(summary = "Persist todo", operationId = "createTodo")
  @Parameter(in = ParameterIn.PATH, name = "sectionId", schema = @Schema(type = "string"))
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "201",
            description = "Todo successfully created",
            content = @Content(schema = @Schema(implementation = UserTodoList.class))),
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
  public ResponseEntity<Mono<UserTodoList>> createTodo(
      @Parameter(hidden = true) @AuthenticationPrincipal
          OAuth2AuthenticatedPrincipal oAuth2AuthenticatedPrincipal,
      @RequestBody Mono<Request> requestTodo) {
    return new ResponseEntity<>(
        todoService.create(getAuthenticateUserEmail(oAuth2AuthenticatedPrincipal), requestTodo),
        HttpStatus.CREATED);
  }

  @Operation(summary = "Get all todos", operationId = "getAllTodo")
  @Parameter(in = ParameterIn.PATH, name = "sectionId", schema = @Schema(type = "string"))
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Retrieved all todos",
            content = @Content(schema = @Schema(implementation = UserTodoList.class))),
        @ApiResponse(
            responseCode = "401",
            description = "Unauthorized",
            content = {@Content(schema = @Schema)})
      })
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Flux<UserTodoList> getAllTodo() {
    return todoService.findAll();
  }

  @Operation(summary = "Update todo", operationId = "updateTodo")
  @Parameter(in = ParameterIn.PATH, name = "sectionId", schema = @Schema(type = "string"))
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Todo successfully updated",
            content = @Content(schema = @Schema(implementation = UserTodoList.class))),
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
  public ResponseEntity<Mono<UserTodoList>> updateTodo(
      @Parameter(hidden = true) @AuthenticationPrincipal
          OAuth2AuthenticatedPrincipal oAuth2AuthenticatedPrincipal,
      @RequestBody Mono<Request> todo,
      @PathVariable Integer id) {
    return new ResponseEntity<Mono<UserTodoList>>(
        todoService.update(getAuthenticateUserEmail(oAuth2AuthenticatedPrincipal), todo, id),
        HttpStatus.OK);
  }

  @Operation(summary = "Delete todo by id", operationId = "deleteTodo")
  @Parameter(in = ParameterIn.PATH, name = "sectionId", schema = @Schema(type = "string"))
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
  public ResponseEntity<Mono<Void>> deleteTodo(
      @AuthenticationPrincipal OAuth2AuthenticatedPrincipal oAuth2AuthenticatedPrincipal,
      @PathVariable Integer id) {
    var httpHeader = new HttpHeaders();
    httpHeader.add("id", String.valueOf(id));
    return id == null || id < 0
        ? new ResponseEntity<Mono<Void>>(httpHeader, HttpStatus.BAD_REQUEST)
        : new ResponseEntity<Mono<Void>>(
            todoService.delete(getAuthenticateUserEmail(oAuth2AuthenticatedPrincipal), id),
            HttpStatus.OK);
  }

  @Operation(summary = "Get standard tags", operationId = "getStandardTags")
  @Parameter(in = ParameterIn.PATH, name = "sectionId", schema = @Schema(type = "string"))
  @ApiResponses(
      @ApiResponse(
          responseCode = "200",
          description = "StandardTags retrieved successfully",
          content = {@Content(schema = @Schema)}))
  @GetMapping("/standardTags")
  public Flux<StandardTags> getStandardTags() {
    return Flux.fromStream(Arrays.stream(StandardTags.values()));
  }

  private String getAuthenticateUserEmail(
      OAuth2AuthenticatedPrincipal oAuth2AuthenticatedPrincipal) {
    return String.valueOf(oAuth2AuthenticatedPrincipal.getAttributes().get("email"));
  }
}
