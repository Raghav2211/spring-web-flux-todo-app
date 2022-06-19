package com.spring.webflux.todo.router;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.*;

import com.spring.webflux.todo.dto.TodoResource;
import com.spring.webflux.todo.entity.Todo;
import com.spring.webflux.todo.exception.InvalidTodoException;
import com.spring.webflux.todo.exception.TodoRuntimeException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.function.Function;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Configuration(proxyBeanMethods = false)
public class TodoRouter {

  public static final String REQUEST_HEADER_ID = "id";
  private final Function<? super TodoRuntimeException, Mono<? extends ServerResponse>>
      handleInvalidTodoRequest =
          todoException ->
              todoException instanceof InvalidTodoException
                  ? ServerResponse.status(todoException.getHttpStatus())
                      .headers(
                          httpHeaders ->
                              httpHeaders.add(
                                  REQUEST_HEADER_ID,
                                  ((InvalidTodoException) todoException).getTodoId().toString()))
                      .build()
                  : ServerResponse.status(todoException.getHttpStatus()).build();

  @Bean
  @RouterOperations({
    @RouterOperation(
        path = "/api/v2/todo/{id}",
        produces = {MediaType.APPLICATION_JSON_VALUE},
        beanClass = TodoRouteHandler.class,
        method = RequestMethod.GET,
        beanMethod = "getTodoById",
        operation =
            @Operation(
                summary = "Get a todo by its id",
                operationId = "getTodoById",
                responses = {
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
                },
                parameters = {@Parameter(in = ParameterIn.PATH, name = REQUEST_HEADER_ID)})),
    @RouterOperation(
        path = "/api/v2/todo/{id}",
        produces = {MediaType.APPLICATION_JSON_VALUE},
        beanClass = TodoRouteHandler.class,
        method = RequestMethod.PUT,
        beanMethod = "updateTodo",
        operation =
            @Operation(
                summary = "Update todo",
                operationId = "updateTodo",
                responses = {
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
                },
                requestBody =
                    @RequestBody(
                        content = @Content(schema = @Schema(implementation = TodoResource.class))),
                parameters = {@Parameter(in = ParameterIn.PATH, name = REQUEST_HEADER_ID)})),
    @RouterOperation(
        path = "/api/v2/todo",
        produces = {MediaType.APPLICATION_JSON_VALUE},
        beanClass = TodoRouteHandler.class,
        method = RequestMethod.POST,
        beanMethod = "createTodo",
        operation =
            @Operation(
                summary = "Persist todo",
                operationId = "createTodo",
                responses = {
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
                },
                requestBody =
                    @RequestBody(
                        content =
                            @Content(schema = @Schema(implementation = TodoResource.class))))),
    @RouterOperation(
        path = "/api/v2/todo",
        produces = {MediaType.APPLICATION_JSON_VALUE},
        beanClass = TodoRouteHandler.class,
        method = RequestMethod.GET,
        beanMethod = "getAllTodo",
        operation =
            @Operation(
                summary = "Get all todos",
                operationId = "getAllTodo",
                responses = {
                  @ApiResponse(
                      responseCode = "200",
                      description = "Retrieved all todos",
                      content = @Content(schema = @Schema(implementation = Todo.class))),
                  @ApiResponse(
                      responseCode = "401",
                      description = "Unauthorized",
                      content = {@Content(schema = @Schema)})
                })),
    @RouterOperation(
        path = "/api/v2/todo/{id}",
        method = RequestMethod.DELETE,
        beanClass = TodoRouteHandler.class,
        beanMethod = "deleteTodo",
        operation =
            @Operation(
                summary = "Delete todo by id",
                operationId = "deleteTodo",
                responses = {
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
                },
                parameters = {@Parameter(in = ParameterIn.PATH, name = REQUEST_HEADER_ID)})),
  })
  public RouterFunction<ServerResponse> route(TodoRouteHandler todoRouteHandler) {
    return nest(
        path("/api/v2/todo"),
        nest(
            accept(APPLICATION_JSON).or(contentType(APPLICATION_JSON)),
            RouterFunctions.route(GET(""), todoRouteHandler::getAllTodo)
                .andRoute(
                    method(HttpMethod.POST),
                    serverRequest ->
                        todoRouteHandler
                            .createTodo(serverRequest)
                            .onErrorResume(TodoRuntimeException.class, handleInvalidTodoRequest))
                .andNest(
                    path("/{id:[0-9]+}"),
                    RouterFunctions.route(
                            method(HttpMethod.GET),
                            serverRequest ->
                                todoRouteHandler
                                    .getTodoById(serverRequest)
                                    .onErrorResume(
                                        InvalidTodoException.class, handleInvalidTodoRequest))
                        .andRoute(
                            method(HttpMethod.PUT),
                            serverRequest ->
                                todoRouteHandler
                                    .updateTodo(serverRequest)
                                    .onErrorResume(
                                        TodoRuntimeException.class, handleInvalidTodoRequest)
                                    .onErrorResume(
                                        InvalidTodoException.class, handleInvalidTodoRequest))
                        .andRoute(
                            method(HttpMethod.DELETE),
                            serverRequest ->
                                todoRouteHandler
                                    .deleteTodo(serverRequest)
                                    .onErrorResume(
                                        InvalidTodoException.class, handleInvalidTodoRequest)))
                .andRoute(path("/{id}"), this::badRequest)));
  }

  private Mono<ServerResponse> badRequest(ServerRequest request) {
    return ServerResponse.badRequest()
        .headers(
            httpHeaders ->
                httpHeaders.add(REQUEST_HEADER_ID, request.pathVariable(REQUEST_HEADER_ID)))
        .build();
  }
}
