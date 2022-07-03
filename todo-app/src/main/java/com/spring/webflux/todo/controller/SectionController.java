package com.spring.webflux.todo.controller;

import com.spring.webflux.todo.dto.request.SectionRequest;
import com.spring.webflux.todo.dto.response.SectionResponse;
import com.spring.webflux.todo.exception.InvalidSectionRuntimeException;
import com.spring.webflux.todo.mapper.SectionMapper;
import com.spring.webflux.todo.repository.SectionRepository;
import com.spring.webflux.todo.security.SecurityUtils;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = {"/api/v1/section"})
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class SectionController {
  private final SectionRepository sectionRepository;

  @PostMapping(
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  public Mono<SectionResponse> createSection(
      @Parameter(hidden = true) @AuthenticationPrincipal
          OAuth2AuthenticatedPrincipal oAuth2AuthenticatedPrincipal,
      @RequestBody Mono<SectionRequest> requestMono) {
    return requestMono
        .flatMap(
            sectionRequest ->
                sectionRepository
                    .save(
                        SectionMapper.INSTANCE.requestToEntity(
                            SecurityUtils.getUserId(oAuth2AuthenticatedPrincipal), sectionRequest))
                    .switchIfEmpty(
                        Mono.error(new InvalidSectionRuntimeException(sectionRequest.getName()))))
        .map(section -> SectionMapper.INSTANCE.entityToResponse(section));
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Flux<SectionResponse> getAllSectionByUser(
      @Parameter(hidden = true) @AuthenticationPrincipal
          OAuth2AuthenticatedPrincipal oAuth2AuthenticatedPrincipal) {
    return sectionRepository
        .findByUserId(SecurityUtils.getUserId(oAuth2AuthenticatedPrincipal))
        .map(section -> SectionMapper.INSTANCE.entityToResponse(section));
  }

  private String getAuthenticateUserEmail(
      OAuth2AuthenticatedPrincipal oAuth2AuthenticatedPrincipal) {
    return String.valueOf(oAuth2AuthenticatedPrincipal.getAttributes().get("email"));
  }
}
