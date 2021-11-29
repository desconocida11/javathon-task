package ru.filit.mdma.web.controller;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.filit.mdma.service.ClientService;
import ru.filit.mdma.web.dto.AccountNumberDto;
import ru.filit.mdma.web.dto.ClientDto;
import ru.filit.mdma.web.dto.ClientIdDto;
import ru.filit.mdma.web.dto.ClientLevelDto;
import ru.filit.mdma.web.dto.ClientSearchDto;
import ru.filit.mdma.web.dto.ContactDto;
import ru.filit.mdma.web.dto.LoanPaymentDto;
import ru.filit.mdma.web.dto.OperationDto;


/**
 * Контроллер действий с клиентами.
 */
@RestController
@RequestMapping
@AllArgsConstructor
public class ClientApiImpl implements ClientApi {

  private final ClientService clientService;

  @Override
  public ResponseEntity<List<ClientDto>> findClient(ClientSearchDto clientSearchDto) {
    return clientService.findClient(clientSearchDto);
  }

  @Override
  public ResponseEntity<ClientDto> getClient(ClientIdDto clientIdDto) {
    return clientService.getClient(clientIdDto);
  }

  @Override
  public ResponseEntity<ClientLevelDto> getClientLevel(ClientIdDto clientIdDto) {
    return clientService.getClientLevel(clientIdDto);
  }

  @Override
  public ResponseEntity<List<OperationDto>> getLastOperations(AccountNumberDto accountNumberDto) {
    return clientService.getAccountOperations(accountNumberDto);
  }

  @Override
  public ResponseEntity<LoanPaymentDto> getLoanPayment(AccountNumberDto accountNumberDto) {
    return clientService.getLoanPayment(accountNumberDto);
  }

  @Override
  public ResponseEntity<ContactDto> saveContact(ContactDto contactDto) {
    return clientService.saveContact(contactDto);
  }
}
