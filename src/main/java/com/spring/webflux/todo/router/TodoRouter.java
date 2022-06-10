package com.spring.webflux.todo.router;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.*;

import com.spring.webflux.todo.dto.TodoResource;
import com.spring.webflux.todo.entity.Todo;
import com.spring.webflux.todo.exception.TodoException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
                operationId = "getTodoById",
                responses = {
                  @ApiResponse(
                      responseCode = "200",
                      description = "Retrieved todo successfully",
                      content = @Content(schema = @Schema(implementation = Todo.class))),
                  @ApiResponse(
                      responseCode = "404",
                      description = "Todo not found",
                      headers = {@Header(name = "id")}),
                  @ApiResponse(
                      responseCode = "400",
                      description = "Bad Request",
                      headers = {@Header(name = "id")}),
                  @ApiResponse(responseCode = "401", description = "Unauthorized")
                },
                parameters = {@Parameter(in = ParameterIn.PATH, name = "id")})),
    @RouterOperation(
        path = "/api/v2/todo/{id}",
        produces = {MediaType.APPLICATION_JSON_VALUE},
        beanClass = TodoRouteHandler.class,
        method = RequestMethod.PUT,
        beanMethod = "updateTodo",
        operation =
            @Operation(
                operationId = "updateTodo",
                responses = {
                  @ApiResponse(
                      responseCode = "200",
                      description = "Todo successfully updated",
                      content = @Content(schema = @Schema(implementation = Todo.class))),
                  @ApiResponse(
                      responseCode = "400",
                      description = "Todo[id] not found",
                      content = @Content(schema = @Schema(implementation = TodoException.class))),
                  @ApiResponse(responseCode = "401", description = "Unauthorized")
                },
                requestBody =
                    @RequestBody(
                        content = @Content(schema = @Schema(implementation = TodoResource.class))),
                parameters = {@Parameter(in = ParameterIn.PATH, name = "id")})),
    @RouterOperation(
        path = "/api/v2/todo",
        produces = {MediaType.APPLICATION_JSON_VALUE},
        beanClass = TodoRouteHandler.class,
        method = RequestMethod.POST,
        beanMethod = "createTodo",
        operation =
            @Operation(
                operationId = "createTodo",
                responses = {
                  @ApiResponse(
                      responseCode = "201",
                      description = "Todo successfully created",
                      content = @Content(schema = @Schema(implementation = Todo.class))),
                  @ApiResponse(responseCode = "401", description = "Unauthorized")
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
                operationId = "getAllTodo",
                responses = {
                  @ApiResponse(
                      responseCode = "200",
                      description = "Retrieved all todos",
                      content = @Content(schema = @Schema(implementation = Todo.class))),
                  @ApiResponse(responseCode = "401", description = "Unauthorized")
                })),
    @RouterOperation(
        path = "/api/v2/todo/{id}",
        method = RequestMethod.DELETE,
        beanClass = TodoRouteHandler.class,
        beanMethod = "deleteTodo",
        operation =
            @Operation(
                operationId = "deleteTodo",
                responses = {
                  @ApiResponse(responseCode = "204", description = "Todo successfully deleted"),
                  @ApiResponse(responseCode = "400", description = "No Todo with id [id] exists!"),
                  @ApiResponse(responseCode = "500", description = "No Todo with id exists!")
                },
                parameters = {@Parameter(in = ParameterIn.PATH, name = "id")})),
  })
  public RouterFunction<ServerResponse> route(TodoRouteHandler todoRouteHandler) {
    return nest(
        path("/api/v2/todo"),
        nest(
            accept(APPLICATION_JSON).or(contentType(APPLICATION_JSON)),
            RouterFunctions.route(GET(""), todoRouteHandler::getAllTodo)
                .andRoute(method(HttpMethod.POST), todoRouteHandler::createTodo)
                .andNest(
                    path("/{id:[0-9]+}"),
                    RouterFunctions.route(method(HttpMethod.GET), todoRouteHandler::getTodoById)
                        .andRoute(method(HttpMethod.PUT), todoRouteHandler::updateTodo)
                        .andRoute(method(HttpMethod.DELETE), todoRouteHandler::deleteTodo))
                .andRoute(GET("/{id}"), this::badRequest)));
  }

  private Mono<ServerResponse> badRequest(ServerRequest request) {
    return ServerResponse.badRequest()
        .headers(httpHeaders -> httpHeaders.add("id", request.pathVariable("id")))
        .build();
  }
}
