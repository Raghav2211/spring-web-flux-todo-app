package com.spring.webflux.todo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

@SpringBootApplication
@RestController
public class EdgeApplication {

  public static void main(String[] args) {
    SpringApplication.run(EdgeApplication.class, args);
  }

  @GetMapping("/")
  public Mono<Token> index(
      @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient,
      WebSession session) {
    return Mono.just(new Token(authorizedClient.getAccessToken().getTokenValue(), session.getId()));
  }

  @GetMapping("/todoRequestFallback")
  public Mono<String> todoRequestFallback() {
    return Mono.just("default fallback if todo app is unreachable");
  }
}
