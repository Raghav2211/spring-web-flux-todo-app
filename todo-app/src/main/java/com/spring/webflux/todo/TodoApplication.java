package com.spring.webflux.todo;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
    info = @Info(title = "Todo app", version = "1.0", description = "Todo WebFlux Rest API"))
public class TodoApplication {
  public static void main(String[] args) {
    SpringApplication.run(TodoApplication.class, args);
  }
}
