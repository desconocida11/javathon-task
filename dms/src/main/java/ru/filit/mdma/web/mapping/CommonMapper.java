package ru.filit.mdma.web.mapping;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.filit.mdma.web.dto.AccessRequestDto;

/**
 * @author A.Khalitova 04-Dec-2021
 */
@Component
public class CommonMapper {

  @Value("${data-masking.access.version}")
  private String accessVersion;

  public AccessRequestDto getAccessRequest(String crMUserRole) {
    AccessRequestDto accessRequestDto = new AccessRequestDto();
    accessRequestDto.setRole(crMUserRole);
    accessRequestDto.setVersion(accessVersion);
    return accessRequestDto;
  }
}
