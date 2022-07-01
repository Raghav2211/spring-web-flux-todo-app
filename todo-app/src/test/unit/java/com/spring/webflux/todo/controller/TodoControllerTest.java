package com.spring.webflux.todo.controller;

import com.spring.webflux.todo.api.AbstractTodoTest;
import com.spring.webflux.todo.repository.TodoRepository;
import com.spring.webflux.todo.security.SecurityConfig;
import com.spring.webflux.todo.service.TodoService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@Import({SecurityConfig.class, TodoService.class, TodoRepository.class})
@WebFluxTest(controllers = TodoController.class)
public class TodoControllerTest extends AbstractTodoTest {

  private static final String TODO_ROOT_PATH = "/api/v1/todo";

  @Override
  protected String apiRootPath() {
    return TODO_ROOT_PATH;
  }
}
