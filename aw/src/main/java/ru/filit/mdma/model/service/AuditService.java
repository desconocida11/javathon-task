package ru.filit.mdma.model.service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.filit.mdma.model.audit.kafka.AuditMessage;

/**
 * @author A.Khalitova 08-Dec-2021
 */
@Service
@Slf4j
public class AuditService {

  private static final DateTimeFormatter DATE_TIME_FORMATTER =
      DateTimeFormatter.ofPattern("dd.MM.uuuu HH:mm:ss");

  @Value("${dm.repo.location}")
  private String auditDir;

  public void writeMessage(AuditMessage message) {
    try (FileWriter fw = new FileWriter(auditDir + "/dm-audit.txt", true);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter out = new PrintWriter(bw)) {
      Instant instant = Instant.ofEpochMilli(Long.parseLong(message.getTimestamp()));
      LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
      out.println(String.format("%-19s %-8s %-15s %-1s%-24s %s",
          dateTime.format(DATE_TIME_FORMATTER), message.getId(), message.getUserName(),
          message.getRequest(), message.getMethodName(), message.getBody()));
    } catch (IOException e) {
      log.error("Error [{}] while writing to file {}/dm-audit.txt", e.getMessage(), auditDir);
    }
  }
}
