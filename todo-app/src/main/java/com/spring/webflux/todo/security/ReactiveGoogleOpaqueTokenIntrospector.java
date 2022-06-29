package com.spring.webflux.todo.security;

import java.net.URI;
import java.time.Instant;
import java.util.*;
import java.util.function.Predicate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.BadOpaqueTokenException;
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionAuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionException;
import org.springframework.security.oauth2.server.resource.introspection.ReactiveOpaqueTokenIntrospector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class ReactiveGoogleOpaqueTokenIntrospector implements ReactiveOpaqueTokenIntrospector {
  private final Predicate<HttpStatus> is200 = HttpStatus.OK::equals;
  private static final String UER_PROFILE_SCOPE = "userinfo.profile";
  private static final String UER_EMAIL_SCOPE = "userinfo.email";
  private static final ParameterizedTypeReference<Map<String, Object>> STRING_OBJECT_MAP =
      new ParameterizedTypeReference<>() {};
  private final String introspectionUri;

  private final WebClient webClient;

  public ReactiveGoogleOpaqueTokenIntrospector(String introspectionUri) {
    this.introspectionUri = introspectionUri;
    this.webClient = WebClient.builder().build();
  }

  @Override
  public Mono<OAuth2AuthenticatedPrincipal> introspect(String token) {
    return Mono.just(token)
        .map(this::makeRequest)
        .flatMap(this::process)
        .map(this::convertClaimsSet)
        .onErrorMap((e) -> !(e instanceof OAuth2IntrospectionException), this::onError);
  }

  private WebClient.ResponseSpec makeRequest(String token) {
    URI uri = URI.create(introspectionUri + "?access_token=" + token);
    return this.webClient
        .get()
        .uri(uri)
        .header("Accept", new String[] {"application/json"})
        .retrieve();
  }

  private Mono<Map<String, Object>> process(WebClient.ResponseSpec responseSpec) {
    return responseSpec
        .onStatus(
            HttpStatus.BAD_REQUEST::equals,
            clientResponse ->
                Mono.error(() -> new BadOpaqueTokenException("Provided token isn't active")))
        .onStatus(
            Predicate.not(is200),
            clientResponse ->
                Mono.error(
                    () ->
                        new OAuth2IntrospectionException(
                            "Introspection endpoint responded with "
                                + clientResponse.statusCode())))
        .bodyToMono(STRING_OBJECT_MAP)
        .filter((body) -> body.containsKey("expires_in"))
        .switchIfEmpty(Mono.error(() -> new OAuth2IntrospectionException("Invalid token")));
  }

  private OAuth2AuthenticatedPrincipal convertClaimsSet(Map<String, Object> claims) {
    claims.computeIfPresent(
        "aud", (k, v) -> v instanceof String ? Collections.singletonList(v) : v);
    claims.computeIfPresent(
        "exp", (k, v) -> Instant.ofEpochSecond(Long.parseLong(String.valueOf(v))));
    Collection<GrantedAuthority> authorities = new HashSet<>();
    claims.computeIfPresent(
        "scope",
        (k, v) -> {
          if (!(v instanceof String)) {
            return v;
          } else {
            Collection<String> scopes = Arrays.asList(((String) v).split(" "));
            for (String scope : scopes) {
              if (scope.contains(UER_PROFILE_SCOPE) || scope.contains(UER_EMAIL_SCOPE))
                authorities.add(new SimpleGrantedAuthority("SCOPE_USER"));
            }

            return scopes;
          }
        });
    return new OAuth2IntrospectionAuthenticatedPrincipal(claims, authorities);
  }

  private OAuth2IntrospectionException onError(Throwable ex) {
    return new OAuth2IntrospectionException(ex.getMessage(), ex);
  }
}
