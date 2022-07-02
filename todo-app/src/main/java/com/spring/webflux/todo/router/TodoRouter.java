package com.spring.webflux.todo.router;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.*;

import com.spring.webflux.todo.dto.request.TodoRequest;
import com.spring.webflux.todo.dto.response.TodoResponse;
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
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

@Configuration(proxyBeanMethods = false)
@Slf4j
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
        path = "/api/v2/{sectionId}/todo/{id}",
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
                      content = @Content(schema = @Schema(implementation = TodoResponse.class))),
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
                parameters = {
                  @Parameter(
                      in = ParameterIn.PATH,
                      name = "id",
                      schema = @Schema(type = "integer")),
                  @Parameter(
                      in = ParameterIn.PATH,
                      name = "sectionId",
                      schema = @Schema(type = "string"))
                },
                security = {@SecurityRequirement(name = "bearerAuth")})),
    @RouterOperation(
        path = "/api/v2/{sectionId}/todo/{id}",
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
                      content = @Content(schema = @Schema(implementation = TodoResponse.class))),
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
                        content = @Content(schema = @Schema(implementation = TodoRequest.class))),
                parameters = {
                  @Parameter(
                      in = ParameterIn.PATH,
                      name = "id",
                      schema = @Schema(type = "integer")),
                  @Parameter(
                      in = ParameterIn.PATH,
                      name = "sectionId",
                      schema = @Schema(type = "string"))
                },
                security = {@SecurityRequirement(name = "bearerAuth")})),
    @RouterOperation(
        path = "/api/v2/{sectionId}/todo",
        produces = {MediaType.APPLICATION_JSON_VALUE},
        beanClass = TodoRouteHandler.class,
        method = RequestMethod.POST,
        beanMethod = "createTodo",
        operation =
            @Operation(
                summary = "Persist todo",
                operationId = "createTodo",
                parameters = {
                  @Parameter(
                      in = ParameterIn.PATH,
                      name = "sectionId",
                      schema = @Schema(type = "string"))
                },
                responses = {
                  @ApiResponse(
                      responseCode = "201",
                      description = "Todo successfully created",
                      content = @Content(schema = @Schema(implementation = TodoResponse.class))),
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
                        content = @Content(schema = @Schema(implementation = TodoRequest.class))),
                security = {@SecurityRequirement(name = "bearerAuth")})),
    @RouterOperation(
        path = "/api/v2/{sectionId}/todo",
        produces = {MediaType.APPLICATION_JSON_VALUE},
        beanClass = TodoRouteHandler.class,
        method = RequestMethod.GET,
        beanMethod = "getAllTodo",
        operation =
            @Operation(
                summary = "Get all todos",
                operationId = "getAllTodo",
                parameters = {
                  @Parameter(
                      in = ParameterIn.PATH,
                      name = "sectionId",
                      schema = @Schema(type = "string"))
                },
                responses = {
                  @ApiResponse(
                      responseCode = "200",
                      description = "Retrieved all todos",
                      content = @Content(schema = @Schema(implementation = TodoResponse.class))),
                  @ApiResponse(
                      responseCode = "401",
                      description = "Unauthorized",
                      content = {@Content(schema = @Schema)})
                },
                security = {@SecurityRequirement(name = "bearerAuth")})),
    @RouterOperation(
        path = "/api/v2/{sectionId}/todo/{id}",
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
                parameters = {
                  @Parameter(
                      in = ParameterIn.PATH,
                      name = "id",
                      schema = @Schema(type = "integer")),
                  @Parameter(
                      in = ParameterIn.PATH,
                      name = "sectionId",
                      schema = @Schema(type = "string"))
                },
                security = {@SecurityRequirement(name = "bearerAuth")})),
    @RouterOperation(
        path = "/api/v2/{sectionId}/todo/standardTags",
        method = RequestMethod.GET,
        beanClass = TodoRouteHandler.class,
        beanMethod = "getStandardTags",
        operation =
            @Operation(
                summary = "Get standard tags",
                operationId = "getStandardTags",
                parameters = {
                  @Parameter(
                      in = ParameterIn.PATH,
                      name = "sectionId",
                      schema = @Schema(type = "string"))
                },
                responses = {
                  @ApiResponse(
                      responseCode = "200",
                      description = "StandardTags retrieved successfully",
                      content = {@Content(schema = @Schema)})
                },
                security = {@SecurityRequirement(name = "bearerAuth")}))
  })
  public RouterFunction<ServerResponse> route(TodoRouteHandler todoRouteHandler) {
    return nest(
        path("/api/v2/{sectionId}/todo"),
        nest(
            accept(APPLICATION_JSON).or(contentType(APPLICATION_JSON)),
            RouterFunctions.route(GET(""), routeToFindAllTodo(todoRouteHandler))
                .andRoute(method(HttpMethod.POST), routeToCreateTodo(todoRouteHandler))
                .andNest(
                    path("/{id:[0-9]+}"),
                    RouterFunctions.route(
                            method(HttpMethod.GET), routeToFindTodoById(todoRouteHandler))
                        .andRoute(method(HttpMethod.PUT), routeToUpdateTodo(todoRouteHandler))
                        .andRoute(
                            method(HttpMethod.DELETE), routeToDeleteTodoById(todoRouteHandler)))
                .andRoute(path("/standardTags"), routeToGetStandardTags(todoRouteHandler))
                .andRoute(path("/{id}"), this::badRequest)));
  }

  private HandlerFunction<ServerResponse> routeToGetStandardTags(
      TodoRouteHandler todoRouteHandler) {
    return serverRequest -> todoRouteHandler.getStandardTags();
  }

  private HandlerFunction<ServerResponse> routeToFindAllTodo(TodoRouteHandler todoRouteHandler) {
    return serverRequest ->
        todoRouteHandler.getAllTodo(
            getAuthenticatedPrincipal(serverRequest), getSectionId(serverRequest));
  }

  private HandlerFunction<ServerResponse> routeToDeleteTodoById(TodoRouteHandler todoRouteHandler) {
    return serverRequest ->
        todoRouteHandler
            .deleteTodo(
                getAuthenticatedPrincipal(serverRequest),
                getSectionId(serverRequest),
                getTodoId(serverRequest))
            .onErrorResume(InvalidTodoException.class, handleInvalidTodoRequest);
  }

  private HandlerFunction<ServerResponse> routeToFindTodoById(TodoRouteHandler todoRouteHandler) {
    return serverRequest ->
        todoRouteHandler
            .getTodoById(
                getAuthenticatedPrincipal(serverRequest),
                getSectionId(serverRequest),
                getTodoId(serverRequest))
            .onErrorResume(InvalidTodoException.class, handleInvalidTodoRequest);
  }

  private HandlerFunction<ServerResponse> routeToUpdateTodo(TodoRouteHandler todoRouteHandler) {
    return serverRequest ->
        todoRouteHandler
            .updateTodo(
                getAuthenticatedPrincipal(serverRequest),
                getSectionId(serverRequest),
                getTodoId(serverRequest),
                serverRequest)
            .onErrorResume(TodoRuntimeException.class, handleInvalidTodoRequest)
            .onErrorResume(InvalidTodoException.class, handleInvalidTodoRequest);
  }

  private HandlerFunction<ServerResponse> routeToCreateTodo(TodoRouteHandler todoRouteHandler) {
    return serverRequest ->
        todoRouteHandler
            .createTodo(
                getAuthenticatedPrincipal(serverRequest),
                getSectionId(serverRequest),
                serverRequest)
            .onErrorResume(TodoRuntimeException.class, handleInvalidTodoRequest);
  }

  private Mono<ServerResponse> badRequest(ServerRequest request) {
    return ServerResponse.badRequest()
        .headers(
            httpHeaders ->
                httpHeaders.add(REQUEST_HEADER_ID, request.pathVariable(REQUEST_HEADER_ID)))
        .build();
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

  private String getSectionId(ServerRequest request) {
    return request.pathVariable("sectionId");
  }

  private String getTodoId(ServerRequest request) {
    return request.pathVariable("id");
  }
}
