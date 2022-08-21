package com.github.devraghav.springexamples.todo.router;

import com.github.devraghav.springexamples.todo.api.AbstractTodoTest;
import com.github.devraghav.springexamples.todo.security.SecurityConfig;
import com.github.devraghav.springexamples.todo.service.TodoService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
    classes = {
      SecurityConfig.class,
      TodoRouter.class,
      TodoRouteHandler.class,
      TodoService.class,
    })
@WebFluxTest
@Disabled
public class TodoRouterTest extends AbstractTodoTest {
  private static final String TODO_ROOT_PATH = "/api/v2/todo";

  @Override
  protected String apiRootPath() {
    return TODO_ROOT_PATH;
  }
}
