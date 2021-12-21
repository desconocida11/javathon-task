package ru.filit.mdma.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author A.Khalitova 17-Dec-2021
 */
@ResponseStatus(code = HttpStatus.SERVICE_UNAVAILABLE, reason = "Host service is down")
public class ServiceUnavailableException extends RuntimeException {

  public ServiceUnavailableException() {
  }
}
