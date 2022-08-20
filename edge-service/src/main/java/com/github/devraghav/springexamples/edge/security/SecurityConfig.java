package com.github.devraghav.springexamples.edge.security;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.WebFilterChainProxy;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationEntryPoint;

@Configuration
@ConditionalOnClass({EnableWebFluxSecurity.class, WebFilterChainProxy.class})
@ConditionalOnMissingBean({SecurityWebFilterChain.class, WebFilterChainProxy.class})
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@AutoConfigureBefore(ReactiveSecurityAutoConfiguration.class)
public class SecurityConfig {

  @Bean
  SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
    return http.authorizeExchange()
        .pathMatchers("/actuator/health", "/favicon.ico")
        .permitAll()
        .anyExchange()
        .authenticated()
        .and()
        .httpBasic()
        .and()
        .formLogin()
        .and()
        .oauth2Login()
        .and()
        .exceptionHandling()
        .authenticationEntryPoint(
            new RedirectServerAuthenticationEntryPoint("/oauth2/authorization/gateway"))
        .and()
        .build();
  }
}
