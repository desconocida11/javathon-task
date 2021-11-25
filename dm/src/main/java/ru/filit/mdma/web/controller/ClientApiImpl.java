package ru.filit.mdma.web.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.filit.mdma.web.dto.AccountDto;
import ru.filit.mdma.web.dto.AccountNumberDto;
import ru.filit.mdma.web.dto.ClientDto;
import ru.filit.mdma.web.dto.ClientIdDto;
import ru.filit.mdma.web.dto.ClientLevelDto;
import ru.filit.mdma.web.dto.ClientSearchDto;
import ru.filit.mdma.web.dto.ContactDto;
import ru.filit.mdma.web.dto.CurrentBalanceDto;
import ru.filit.mdma.web.dto.LoanPaymentDto;
import ru.filit.mdma.web.dto.OperationDto;
import ru.filit.mdma.web.dto.OperationSearchDto;

/**
 * Контроллер действий с клиентами.
 */
@RestController
@RequestMapping
public class ClientApiImpl implements ClientApi {

  @Override
  public ResponseEntity<List<AccountDto>> getAccount(ClientIdDto clientIdDto) {
    AccountDto accountDto = new AccountDto();
    accountDto.setClientId(clientIdDto.getId());
    accountDto.setCurrency("RUR");
    accountDto.setNumber("40817810853110005823");
    AccountDto accountDto1 = new AccountDto();
    accountDto1.setNumber("40817810452010063617");
    accountDto1.setCurrency("RUR");
    accountDto1.setStatus("ACTIVE");
    return ResponseEntity.status(HttpStatus.OK).body(List.of(accountDto1, accountDto));
  }

  @Override
  public ResponseEntity<CurrentBalanceDto> getAccountBalance(AccountNumberDto accountNumberDto) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
  }

  @Override
  public ResponseEntity<List<OperationDto>> getAccountOperations(
      OperationSearchDto operationSearchDto) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
  }

  @Override
  public ResponseEntity<List<ClientDto>> getClient(ClientSearchDto clientSearchDto) {
    ClientDto clientDto = new ClientDto();
    clientDto.setId(clientSearchDto.getId());
    clientDto.setFirstname("Maria");
    clientDto.setLastname("Иванова");
    return ResponseEntity.status(HttpStatus.OK).body(List.of(clientDto));
  }

  @Override
  public ResponseEntity<ClientLevelDto> getClientLevel(ClientIdDto clientIdDto) {
    ClientLevelDto clientLevelDto = new ClientLevelDto();
    clientLevelDto.setLevel("LOW");
    clientLevelDto.setAvgBalance("55000");
    return ResponseEntity.status(HttpStatus.OK).body(clientLevelDto);
  }

  @Override
  public ResponseEntity<List<ContactDto>> getContact(ClientIdDto clientIdDto) {
    ContactDto contactDto = new ContactDto();
    contactDto.setClientId(clientIdDto.getId());
    contactDto.setId("123");
    contactDto.setValue("000000");
    return ResponseEntity.status(HttpStatus.OK).body(List.of(contactDto));
  }

  @Override
  public ResponseEntity<LoanPaymentDto> getLoanPayment(AccountNumberDto accountNumberDto) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
  }

  @Override
  public ResponseEntity<ContactDto> saveContact(ContactDto contactDto) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
  }
}
