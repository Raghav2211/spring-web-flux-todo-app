package com.spring.webflux.todo.router;

import com.spring.webflux.todo.api.AbstractTodoTest;
import com.spring.webflux.todo.security.SecurityConfig;
import com.spring.webflux.todo.service.TodoService;
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
public class TodoRouterTest extends AbstractTodoTest {
  private static final String TODO_ROOT_PATH = "/api/v2/todo";

  @Override
  protected String apiRootPath() {
    return TODO_ROOT_PATH;
  }
}
