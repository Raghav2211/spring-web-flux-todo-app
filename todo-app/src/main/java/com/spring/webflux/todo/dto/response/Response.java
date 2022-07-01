package com.spring.webflux.todo.dto.response;

import com.spring.webflux.todo.dto.request.Section;
import java.io.Serializable;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

@Data
@Scope(scopeName = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class Response implements Serializable {
  private String userid;
  private Section section;
}
