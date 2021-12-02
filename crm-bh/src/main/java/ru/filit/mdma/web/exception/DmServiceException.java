package ru.filit.mdma.web.exception;

/**
 * @author A.Khalitova 02-Dec-2021
 */
public class DmServiceException extends RuntimeException {

  public DmServiceException() {
    super();
  }

  public DmServiceException(String message) {
    super(message);
  }

  public DmServiceException(String message, Throwable cause) {
    super(message, cause);
  }

  public DmServiceException(Throwable cause) {
    super(cause);
  }
}
