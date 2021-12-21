package ru.filit.mdma.web;

import java.util.OptionalInt;
import java.util.Random;
import java.util.UUID;
import lombok.experimental.UtilityClass;

/**
 * @author A.Khalitova 06-Dec-2021
 */
@UtilityClass
public final class TokenUtil {

  public static String buildToken() {
    String token = UUID.randomUUID().toString().replace("-", "");
    return "#" + token + "#";
  }

  public static boolean isTokenized(String guid) {
    return guid.length() > 2 && guid.startsWith("#") && guid.endsWith("#");
  }

  public static String generateId() {
    final int min = 10_000_000;
    final int max = 99_999_999;
    Random random = new Random();
    OptionalInt optionalInt = random.ints(min, (max + 1)).findFirst();
    if (optionalInt.isPresent()) {
      return String.valueOf(optionalInt.getAsInt());
    }
    // TODO what if not present?
    return "12345678";
  }
}
