package com.github.devraghav.springexamples.edge;

import java.io.Serializable;

public class Token implements Serializable {
  private String value;
  private String sessionId;

  public Token(String value, String sessionId) {
    this.value = value;
    this.sessionId = sessionId;
  }

  public String getValue() {
    return value;
  }

  public String getSessionId() {
    return sessionId;
  }
}
