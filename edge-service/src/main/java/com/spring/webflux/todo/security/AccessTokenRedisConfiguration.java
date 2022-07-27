package com.spring.webflux.todo.security;

import org.springframework.context.annotation.Primary;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Primary
public class AccessTokenRedisConfiguration implements ReactiveOAuth2AuthorizedClientService {
  @Override
  public <T extends OAuth2AuthorizedClient> Mono<T> loadAuthorizedClient(
      String clientRegistrationId, String principalName) {
    return null;
  }

  @Override
  public Mono<Void> saveAuthorizedClient(
      OAuth2AuthorizedClient authorizedClient, Authentication principal) {

    return null;
  }

  @Override
  public Mono<Void> removeAuthorizedClient(String clientRegistrationId, String principalName) {
    return null;
  }
}
