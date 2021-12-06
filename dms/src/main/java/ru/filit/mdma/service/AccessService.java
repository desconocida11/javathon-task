package ru.filit.mdma.service;

import java.util.List;
import ru.filit.mdma.web.dto.AccessDto;
import ru.filit.mdma.web.dto.AccessRequestDto;

/**
 * @author A.Khalitova 05-Dec-2021
 */
public interface AccessService {

  List<AccessDto> getAccess(AccessRequestDto accessRequestDto);
}
