package ru.filit.mdma.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.filit.mdma.web.dto.*;

import java.util.List;


/**
 * Контроллер действий с клиентами.
 */
@RestController
@RequestMapping
public class ClientApiImpl implements ClientApi{

    @Override
    public ResponseEntity<List<ClientDto>> findClient(ClientSearchDto clientSearchDto) {
        return null;
    }

    @Override
    public ResponseEntity<ClientDto> getClient(ClientIdDto clientIdDto) {
        ClientDto clientDto = new ClientDto();
        clientDto.setId("123");
        clientDto.setFirstname("Maria");
        clientDto.setLastname("Иванова");
        return ResponseEntity.status(HttpStatus.OK).body(clientDto);
    }

    @Override
    public ResponseEntity<ClientLevelDto> getClientLevel(ClientIdDto clientIdDto) {
        return null;
    }

    @Override
    public ResponseEntity<List<OperationDto>> getLastOperations(AccountNumberDto accountNumberDto) {
        return null;
    }

    @Override
    public ResponseEntity<LoanPaymentDto> getLoanPayment(AccountNumberDto accountNumberDto) {
        return null;
    }

    @Override
    public ResponseEntity<ContactDto> saveContact(ContactDto contactDto) {
        return null;
    }
}
