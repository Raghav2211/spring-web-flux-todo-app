package com.spring.webflux.todo.controller;

import com.spring.webflux.todo.dto.request.SectionRequest;
import com.spring.webflux.todo.exception.InvalidSectionRuntimeException;
import com.spring.webflux.todo.repository.SectionRepository;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = {"/api/v1/section"})
@SecurityRequirement(name = "bearerAuth")
public class SectionController {
  @Autowired private SectionRepository sectionRepository;

  @PostMapping(
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Mono<Void>> createSection(
      @Parameter(hidden = true) @AuthenticationPrincipal
          OAuth2AuthenticatedPrincipal oAuth2AuthenticatedPrincipal,
      @RequestBody Mono<SectionRequest> requestMono) {
    var addSectionRequest =
        requestMono
            .flatMap(
                sectionRequest ->
                    sectionRepository
                        .addSection(
                            getAuthenticateUserEmail(oAuth2AuthenticatedPrincipal),
                            sectionRequest.getName())
                        .filter(data -> data)
                        .switchIfEmpty(
                            Mono.error(
                                new InvalidSectionRuntimeException(sectionRequest.getName()))))
            .then();
    return new ResponseEntity<Mono<Void>>(addSectionRequest, HttpStatus.CREATED);
  }

  private String getAuthenticateUserEmail(
      OAuth2AuthenticatedPrincipal oAuth2AuthenticatedPrincipal) {
    return String.valueOf(oAuth2AuthenticatedPrincipal.getAttributes().get("email"));
  }
}
