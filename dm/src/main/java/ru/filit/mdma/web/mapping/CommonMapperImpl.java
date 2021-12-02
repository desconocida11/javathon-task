package ru.filit.mdma.web.mapping;

import static java.math.RoundingMode.HALF_UP;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import org.springframework.stereotype.Component;

/**
 * @author A.Khalitova 30-Nov-2021
 */
@Component
public class CommonMapperImpl {

  public String asDto(Number in) {
    return (in == null) ? null : in.toString();
  }

  public String asDto(BigDecimal in) {
    return (in == null) ? null : in.setScale(2, HALF_UP).stripTrailingZeros().toPlainString();
  }

  public Integer asInt(String in) {
    return Integer.parseInt(in);
  }

  public BigDecimal asDecimalEntity(String in) {
    return (in == null) ? null : new BigDecimal(in);
  }

  public Long asLongEntity(String in) {
    return (in == null) ? null : Long.parseLong(in);
  }

  public Long asEpochMilli(LocalDateTime dateTime) {
    return dateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
  }

  public LocalDateTime asDateTime(Long epochMilli) {
    Instant instant = Instant.ofEpochMilli(epochMilli);
    LocalDateTime date = LocalDateTime.ofInstant(instant, ZoneId.of("UTC"));
    return date;
  }
}
