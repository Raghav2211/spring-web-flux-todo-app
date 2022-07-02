package com.spring.webflux.todo.dto.response;

import java.io.Serializable;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

@Data
@Scope(scopeName = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class TodoResponse implements Serializable {}
