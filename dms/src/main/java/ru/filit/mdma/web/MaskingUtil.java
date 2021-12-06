package ru.filit.mdma.web;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;
import lombok.experimental.UtilityClass;
import org.springframework.beans.PropertyAccessor;
import org.springframework.beans.PropertyAccessorFactory;
import ru.filit.mdma.web.dto.AccessDto;

/**
 * @author A.Khalitova 04-Dec-2021
 */
@UtilityClass
public final class MaskingUtil {

  private static final String maskingPattern = "****";

  public static <T extends Serializable> void maskFields(@NotNull T target,
      List<AccessDto> access, @NotNull String entity) {

    Class<? extends Serializable> targetClass = target.getClass();

    Set<String> fieldsToMask = getFieldsToMask(access, entity, targetClass);

    maskFields(fieldsToMask, target);
  }

  public static <T extends Serializable> void maskFields(@NotNull List<T> target,
      List<AccessDto> access, @NotNull String entity) {
    if (target.isEmpty()) {
      return;
    }
    Class<? extends Serializable> targetClass = target.get(0).getClass();

    Set<String> fieldsToMask = getFieldsToMask(access, entity, targetClass);

    target.forEach(t -> maskFields(fieldsToMask, t));
  }

  public static Set<String> getFieldsToMask(List<AccessDto> access, @NotNull String entity,
      Class<? extends Serializable> targetClass) {
    Set<String> visibleFields = access.stream()
        .filter(accessDto -> entity.equals(accessDto.getEntity()))
        .map(AccessDto::getProperty)
        .collect(Collectors.toSet());

    Set<String> fieldsToHide = Arrays.stream(getProperties(targetClass))
    .map(Field::getName).collect(Collectors.toSet());
    fieldsToHide.removeAll(visibleFields);
    return fieldsToHide;
  }

  public Field[] getProperties(Class<? extends Serializable> targetClass) {
    return Arrays.stream(targetClass.getDeclaredFields())
        .filter(field -> {
          int mod = field.getModifiers();
          return !Modifier.isFinal(mod) && !Modifier.isStatic(mod);
        })
        .toArray(Field[]::new);
  }

  private static <T extends Serializable> void maskFields(Set<String> fieldsToMask, T t) {
    PropertyAccessor myAccessor = PropertyAccessorFactory.forBeanPropertyAccess(t);
    for (String field : fieldsToMask) {
      myAccessor.setPropertyValue(field, maskingPattern);
    }
  }
}
