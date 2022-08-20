package com.github.devraghav.springexamples.todo.controller;

import com.github.devraghav.springexamples.todo.api.AbstractTodoTest;
import com.github.devraghav.springexamples.todo.repository.TodoRepository;
import com.github.devraghav.springexamples.todo.security.SecurityConfig;
import com.github.devraghav.springexamples.todo.service.TodoService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@Import({SecurityConfig.class, TodoService.class, TodoRepository.class})
@WebFluxTest(controllers = TodoController.class)
@Disabled
public class TodoControllerTest extends AbstractTodoTest {

  private static final String TODO_ROOT_PATH = "/api/v1/todo";

  @Override
  protected String apiRootPath() {
    return TODO_ROOT_PATH;
  }
}
