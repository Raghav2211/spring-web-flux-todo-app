package com.spring.webflux.todo.security;

import java.net.URI;
import java.time.Instant;
import java.util.*;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.BadOpaqueTokenException;
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionAuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionException;
import org.springframework.security.oauth2.server.resource.introspection.ReactiveOpaqueTokenIntrospector;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class GoogleTokenIntrospector implements ReactiveOpaqueTokenIntrospector {
  private static final ParameterizedTypeReference<Map<String, Object>> STRING_OBJECT_MAP =
      new ParameterizedTypeReference<>() {};
  private final String introspectionUri;

  private final WebClient webClient;

  public GoogleTokenIntrospector(String introspectionUri) {
    this.introspectionUri = introspectionUri;
    this.webClient = WebClient.builder().build();
  }

  @Override
  public Mono<OAuth2AuthenticatedPrincipal> introspect(String token) {
    return Mono.just(token)
        .flatMap(this::makeRequest)
        .flatMap(this::adaptToNimbusResponse)
        .map(this::convertClaimsSet)
        .onErrorMap((e) -> !(e instanceof OAuth2IntrospectionException), this::onError);
  }

  private Mono<ClientResponse> makeRequest(String token) {
    URI uri = URI.create(introspectionUri + "?access_token=" + token);
    return ((WebClient.RequestBodySpec)
            ((WebClient.RequestBodySpec) this.webClient.get().uri(uri))
                .header("Accept", new String[] {"application/json"}))
        .exchange();
  }

  private Mono<Map<String, Object>> adaptToNimbusResponse(ClientResponse responseEntity) {
    return responseEntity.statusCode() != HttpStatus.OK
        ? responseEntity
            .bodyToFlux(DataBuffer.class)
            .map(DataBufferUtils::release)
            .then(
                Mono.error(
                    new OAuth2IntrospectionException(
                        "Introspection endpoint responded with " + responseEntity.statusCode())))
        : responseEntity
            .bodyToMono(STRING_OBJECT_MAP)
            .filter((body) -> body.containsKey("expires_in"))
            .switchIfEmpty(
                Mono.error(
                    () -> new BadOpaqueTokenException("Provided token isn't active or invalid")));
  }

  private OAuth2AuthenticatedPrincipal convertClaimsSet(Map<String, Object> claims) {
    claims.computeIfPresent(
        "aud", (k, v) -> v instanceof String ? Collections.singletonList(v) : v);
    claims.computeIfPresent(
        "exp", (k, v) -> Instant.ofEpochSecond(Long.valueOf(String.valueOf(v))));
    claims.computeIfPresent("iat", (k, v) -> Instant.ofEpochSecond(((Number) v).longValue()));
    claims.computeIfPresent("iss", (k, v) -> v.toString());
    claims.computeIfPresent("nbf", (k, v) -> Instant.ofEpochSecond(((Number) v).longValue()));
    Collection<GrantedAuthority> authorities = new ArrayList();
    claims.computeIfPresent(
        "scope",
        (k, v) -> {
          if (!(v instanceof String)) {
            return v;
          } else {
            Collection<String> scopes = Arrays.asList(((String) v).split(" "));
            Iterator var4 = scopes.iterator();

            while (var4.hasNext()) {
              String scope = (String) var4.next();
              authorities.add(new SimpleGrantedAuthority("SCOPE_" + scope));
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
