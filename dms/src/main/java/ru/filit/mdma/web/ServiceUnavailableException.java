package ru.filit.mdma.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.SERVICE_UNAVAILABLE, reason = "Host service is down")
public class ServiceUnavailableException extends RuntimeException {

  public ServiceUnavailableException() {
  }
}
