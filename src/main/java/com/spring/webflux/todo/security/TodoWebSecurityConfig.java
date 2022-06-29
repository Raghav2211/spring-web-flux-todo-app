package com.spring.webflux.todo.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.server.resource.introspection.ReactiveOpaqueTokenIntrospector;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

@EnableWebFluxSecurity
@Configuration
@Slf4j
public class TodoWebSecurityConfig {

  @Bean
  SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
    return http.csrf()
        .disable()
        .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
        .authorizeExchange()
        .pathMatchers(
            "/actuator/info",
            "/actuator/health",
            "/favicon.ico",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/webjars/**")
        .permitAll()
        .and()
        .authorizeExchange(exchanges -> exchanges.anyExchange().authenticated())
        .oauth2ResourceServer(ServerHttpSecurity.OAuth2ResourceServerSpec::opaqueToken)
        .build();
  }

  @Bean
  public ReactiveOpaqueTokenIntrospector introspector(
      @Value("${spring.security.oauth2.resourceserver.opaque-token.introspection-uri}")
          String introspectionUri) {
    return new GoogleTokenIntrospector(introspectionUri);
  }
}
