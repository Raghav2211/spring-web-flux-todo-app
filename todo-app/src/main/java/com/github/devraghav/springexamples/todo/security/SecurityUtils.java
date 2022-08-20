package com.github.devraghav.springexamples.todo.security;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;

@AllArgsConstructor(access = AccessLevel.NONE)
public final class SecurityUtils {
  public static String getUserId(OAuth2AuthenticatedPrincipal oAuth2AuthenticatedPrincipal) {
    return String.valueOf(oAuth2AuthenticatedPrincipal.getAttributes().get("email"));
  }
}
