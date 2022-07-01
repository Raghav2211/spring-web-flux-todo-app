package com.spring.webflux.todo.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import java.io.Serializable;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@Scope(scopeName = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class Request implements Serializable {
  private final Section section;
//TODO: remove
//  public static void main(String[] args) throws JsonProcessingException {
//    String request =
//        "{\"section\":{\"name\":\"Test Section\",\"todos\":[{\"task\":\"Add test case\",\"schedule\":null,\"tags\":[\"custom\",\"HOME\"]}],\"tags\":[\"custom\",\"HOME\"]}}";
//    ObjectMapper objectMapper = new ObjectMapper();
//    objectMapper.registerModule(new Jdk8Module());
//    System.out.println(new ObjectMapper().readValue(request, Request.class));
//  }
}
