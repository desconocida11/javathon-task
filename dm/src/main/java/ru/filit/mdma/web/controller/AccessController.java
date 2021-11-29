package ru.filit.mdma.web.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.filit.mdma.web.dto.AccessDto;
import ru.filit.mdma.web.dto.AccessRequestDto;
import ru.filit.mdma.web.exception.ClientNotFoundException;

@RestController
public class AccessController implements AccessApi {

  private static final String MANAGER_ROLE = "MANAGER";

  @Override
  public ResponseEntity<List<AccessDto>> getAccess(AccessRequestDto body) {
    if (body.getRole().equals(MANAGER_ROLE)) {
      throw new ClientNotFoundException("For this role client is not present");
    }
    List<AccessDto> accessDtoList = new ArrayList<>();
    accessDtoList.add((new AccessDto()).entity("client").property("id"));
    accessDtoList.add((new AccessDto()).entity("client").property("lastname"));
    return ResponseEntity.ok(accessDtoList);
  }
}