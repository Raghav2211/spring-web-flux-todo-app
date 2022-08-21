package com.github.devraghav.springexamples.edge.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RMapReactive;
import org.redisson.api.RedissonReactiveClient;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Primary
@RequiredArgsConstructor
@Slf4j
public class RedisReactiveOAuth2AuthorizedClientService
    implements ReactiveOAuth2AuthorizedClientService {
  private final RedissonReactiveClient redissonReactiveClient;

  @Override
  public Mono<OAuth2AuthorizedClient> loadAuthorizedClient(
      String clientRegistrationId, String principalName) {
    log.info(
        "loadAuthorizedClient with clientRegistrationId {} & principalName {}",
        clientRegistrationId,
        principalName);
    RMapReactive<String, OAuth2AuthorizedClient> authorizedClient =
        redissonReactiveClient.getMap(principalName);
    return authorizedClient.get(clientRegistrationId);
  }

  @Override
  public Mono<Void> saveAuthorizedClient(
      OAuth2AuthorizedClient authorizedClient, Authentication principal) {
    log.info(
        "saveAuthorizedClient with clientRegistrationId {} & principalName {}",
        authorizedClient.getClientRegistration().getRegistrationId(),
        principal.getName());
    return redissonReactiveClient
        .getMap(principal.getName())
        .put(authorizedClient.getClientRegistration().getRegistrationId(), authorizedClient)
        .then();
  }

  @Override
  public Mono<Void> removeAuthorizedClient(String clientRegistrationId, String principalName) {
    log.info(
        "removeAuthorizedClient with clientRegistrationId {} & principalName {}",
        clientRegistrationId,
        principalName);
    return redissonReactiveClient.getMap(principalName).remove(clientRegistrationId).then();
  }
}
