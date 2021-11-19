package ru.filit.oas.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.filit.oas.dm.web.controller.AccessApi;
import ru.filit.oas.dm.web.dto.AccessDto;
import ru.filit.oas.dm.web.dto.AccessRequestDto;

import java.util.ArrayList;
import java.util.List;

@RestController
public class AccessController implements AccessApi {

    @Override
    public ResponseEntity<List<AccessDto>> getAccess(AccessRequestDto body) {
        List<AccessDto> accessDtoList = new ArrayList<>();
        accessDtoList.add((new AccessDto()).entity("client").property("id"));
        accessDtoList.add((new AccessDto()).entity("client").property("lastname"));
        return ResponseEntity.ok(accessDtoList);
        //return ResponseEntity.badRequest().build();
    }
}
