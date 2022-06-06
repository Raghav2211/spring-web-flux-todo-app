package com.spring.webflux.todo.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
@Configuration
@Slf4j
public class TodoWebSecurityConfig {

  private static final String USER = "USER";

  @Value(value = "${todo.security.basic.auth.username}")
  private String basicAuthUserName;

  @Value(value = "${todo.security.basic.auth.password}")
  private String basicAuthPassWord;

  @Bean
  public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
    return http.csrf()
        .disable()
        .authorizeExchange()
        .pathMatchers("/actuator/info", "/actuator/health")
        .permitAll()
        .and()
        .authorizeExchange()
        .anyExchange()
        .authenticated()
        .and()
        .httpBasic()
        .and()
        .formLogin()
        .and()
        .build();
  }

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public MapReactiveUserDetailsService userDetailsService(BCryptPasswordEncoder passwordEncoder) {
    UserDetails user =
        User.builder()
            .username(basicAuthUserName)
            .password(basicAuthPassWord)
            .passwordEncoder((password) -> passwordEncoder.encode(password))
            .roles(USER)
            .build();
    return new MapReactiveUserDetailsService(user);
  }
}
