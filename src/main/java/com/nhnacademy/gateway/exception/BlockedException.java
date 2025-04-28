package com.nhnacademy.gateway.exception;

import org.springframework.security.core.AuthenticationException;

public class BlockedException extends AuthenticationException {
  public BlockedException(String msg) {
    super(msg);
  }
}
