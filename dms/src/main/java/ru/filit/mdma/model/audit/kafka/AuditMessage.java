package ru.filit.mdma.model.audit.kafka;

import java.io.Serializable;
import java.time.Instant;
import java.util.OptionalInt;
import java.util.Random;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author A.Khalitova 08-Dec-2021
 */
@Getter
@Setter
@NoArgsConstructor
public class AuditMessage implements Serializable {

  private static final long serialVersionUID = 1L;

  private String timestamp;

  private String id;

  private String userName;

  private String methodName;

  private String body;

  private String request;

  public AuditMessage(String userName, String methodName, String body, boolean isRequest,
      String id) {
    long timestamp = Instant.now().getEpochSecond();
    this.timestamp = String.valueOf(timestamp);
    this.id = id;
    this.userName = userName;
    this.methodName = methodName;
    this.body = body;
    if (isRequest) {
      this.request = ">";
    } else {
      this.request = "<";
    }
  }
}
