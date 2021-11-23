package ru.filit.mdma.web.controller;

import org.springframework.http.ResponseEntity;
import ru.filit.mdma.web.dto.AccessDto;
import ru.filit.mdma.web.dto.AccessRequestDto;

import java.util.List;

/**
 * Контроллер прав доступа для роли.
 */
public class AccessApiImpl implements AccessApi {

    @Override
    public ResponseEntity<List<AccessDto>> getAccess(AccessRequestDto accessRequestDto) {
        return null;
    }
}
