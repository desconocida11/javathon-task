package ru.filit.mdma.web.controller;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.filit.mdma.service.ClientService;
import ru.filit.mdma.web.dto.AccountDto;
import ru.filit.mdma.web.dto.AccountNumberDto;
import ru.filit.mdma.web.dto.ClientDto;
import ru.filit.mdma.web.dto.ClientIdDto;
import ru.filit.mdma.web.dto.ClientLevelDto;
import ru.filit.mdma.web.dto.ClientSearchDto;
import ru.filit.mdma.web.dto.ContactDto;
import ru.filit.mdma.web.dto.LoanPaymentDto;
import ru.filit.mdma.web.dto.OperationDto;
import ru.filit.mdma.web.dto.OperationSearchDto;
import ru.filit.mdma.web.mapping.DtoMapper;


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
    final ResponseEntity<List<ClientDto>> client = clientService
        .findClientById(clientIdDto);
    if (client.getStatusCode() != HttpStatus.OK
        || client.getBody() == null || client.getBody().size() != 1) {
      return ResponseEntity.status(client.getStatusCode()).build();
    }
    // TODO refactor: type erasure issue, LinkedHashMap to POJO in response body
    final ClientDto body = new ClientDto();
    final ResponseEntity<List<ContactDto>> contact = clientService.getContact(clientIdDto);
    if (contact.getStatusCode() != HttpStatus.OK
        && contact.getBody() != null) {
      body.setContacts(new ArrayList<>(contact.getBody()));

    }
    final ResponseEntity<List<AccountDto>> account = clientService.getAccount(clientIdDto);
    if (account.getStatusCode() != HttpStatus.OK
        && account.getBody() != null) {
      body.setAccounts(new ArrayList<>(account.getBody()));
    }
    return ResponseEntity.status(HttpStatus.OK).body(body);
  }

  @Override
  public ResponseEntity<ClientLevelDto> getClientLevel(ClientIdDto clientIdDto) {
    return clientService.getClientLevel(clientIdDto);
  }

  @Override
  public ResponseEntity<List<OperationDto>> getLastOperations(AccountNumberDto accountNumberDto) {
    OperationSearchDto operationSearchDto = DtoMapper.INSTANCE
        .accountNumberToOperationSearch(accountNumberDto);
    return clientService.getAccountOperations(operationSearchDto);
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
