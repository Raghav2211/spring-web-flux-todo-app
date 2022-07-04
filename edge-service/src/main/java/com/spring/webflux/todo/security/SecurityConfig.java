package com.spring.webflux.todo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

@EnableWebFluxSecurity
@Configuration
public class SecurityConfig {

  @Bean
  SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
    return http.csrf()
        .disable()
        .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
        .authorizeExchange()
        .pathMatchers(
            // spotless:off

            // "/actuator/info", <-- https://github.com/spring-projects/spring-boot/issues/24205

            // spotless:on
            "/actuator/health", "/favicon.ico")
        .permitAll()
        .and()
        .authorizeExchange(exchanges -> exchanges.anyExchange().authenticated())
        .oauth2Login(Customizer.withDefaults())
        .build();
  }
}
