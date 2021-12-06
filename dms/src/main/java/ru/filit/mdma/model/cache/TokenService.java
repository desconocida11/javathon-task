package ru.filit.mdma.model.cache;

import static ru.filit.mdma.web.MaskingUtil.getFieldsToMask;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;
import javax.validation.constraints.NotNull;
import org.springframework.beans.PropertyAccessor;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.filit.mdma.web.MaskingUtil;
import ru.filit.mdma.web.TokenUtil;
import ru.filit.mdma.web.dto.AccessDto;

/**
 * @author A.Khalitova 06-Dec-2021
 */
@Component
public class TokenService {

  private final SensitiveDataCache dataCache;

  public TokenService(SensitiveDataCache dataCache) {
    this.dataCache = dataCache;
  }

  public <T extends Serializable> void detokenizeObject(T target) {
    Class<? extends Serializable> targetClass = target.getClass();
    PropertyAccessor myAccessor = PropertyAccessorFactory.forBeanPropertyAccess(target);
    for (Field field : MaskingUtil.getProperties(targetClass)) {
      Object value = myAccessor.getPropertyValue(field.getName());
      if (value instanceof String) {
        if (TokenUtil.isTokenized((String) value)) {
          final String cachedData = dataCache.read((String) value);
          myAccessor.setPropertyValue(field.getName(), cachedData);
        }
      }
    }
  }

  public <T extends Serializable> void tokenizeObject(@NotNull T target,
      List<AccessDto> access, String entity) {
    Class<? extends Serializable> targetClass = target.getClass();
    final Set<String> fieldsToTokenize = getFieldsToMask(access, entity, targetClass);
    tokenizeFields(fieldsToTokenize, target);
  }

  public <T extends Serializable> void tokenizeObject(@NotNull List<T> target,
      List<AccessDto> access, String entity) {
    if (target.isEmpty()) {
      return;
    }
    Class<? extends Serializable> targetClass = target.get(0).getClass();
    final Set<String> fieldsToTokenize = getFieldsToMask(access, entity, targetClass);
    target.forEach(t -> tokenizeFields(fieldsToTokenize, t));
  }

  private <T extends Serializable> void tokenizeFields(Set<String> fieldsToMask, T t) {
    PropertyAccessor myAccessor = PropertyAccessorFactory.forBeanPropertyAccess(t);
    for (String field : fieldsToMask) {
      final Object propertyValue = myAccessor.getPropertyValue(field);
      if (propertyValue instanceof String) {
        final String token = TokenUtil.buildToken();
        if (dataCache.put(token, (String) propertyValue)) {
          myAccessor.setPropertyValue(field, token);
        } else {
          throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
              "Failed to put tokenized data in cache");
        }
      }
    }
  }
}
