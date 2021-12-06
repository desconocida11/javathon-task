package ru.filit.mdma.web.controller;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.filit.mdma.service.AccessService;
import ru.filit.mdma.service.AccountService;
import ru.filit.mdma.service.ClientService;
import ru.filit.mdma.web.dto.AccessDto;
import ru.filit.mdma.web.dto.AccessRequestDto;
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
import ru.filit.mdma.web.mapping.CommonMapper;

/**
 * @author A.Khalitova 03-Dec-2021
 */
@RestController
@RequestMapping
@AllArgsConstructor
public class ApiController implements ClientApi, AccessApi, DummyApi {

  private final ClientService clientService;
  private final AccountService accountService;
  private final AccessService accessService;

  private final CommonMapper commonMapper;

  @Override
  public ResponseEntity<List<AccessDto>> getAccess(AccessRequestDto accessRequestDto) {
    List<AccessDto> access = accessService.getAccess(accessRequestDto);
    return ResponseEntity.ok(access);
  }

  @Override
  public ResponseEntity<List<AccountDto>> getAccount(ClientIdDto clientIdDto, String crMUserRole,
      String crMUserName) {
    List<AccessDto> access = getAccess(commonMapper.getAccessRequest(crMUserRole)).getBody();
    List<AccountDto> accounts = accountService.getAccount(clientIdDto, access);
    return ResponseEntity.ok(accounts);
  }

  @Override
  public ResponseEntity<CurrentBalanceDto> getAccountBalance(AccountNumberDto accountNumberDto,
      String crMUserRole, String crMUserName) {
    List<AccessDto> access = getAccess(commonMapper.getAccessRequest(crMUserRole)).getBody();
    CurrentBalanceDto accountBalance = accountService.getAccountBalance(accountNumberDto, access);
    return ResponseEntity.ok(accountBalance);
  }

  @Override
  public ResponseEntity<List<OperationDto>> getAccountOperations(
      OperationSearchDto operationSearchDto, String crMUserRole, String crMUserName) {
    List<AccessDto> access = getAccess(commonMapper.getAccessRequest(crMUserRole)).getBody();
    List<OperationDto> accountOperations = accountService.getAccountOperations(operationSearchDto,
        access);
    return ResponseEntity.ok(accountOperations);
  }

  @Override
  public ResponseEntity<List<ClientDto>> getClient(ClientSearchDto clientSearchDto,
      String crMUserRole, String crMUserName) {
    List<AccessDto> accessEntity = getAccess(
        commonMapper.getAccessRequest(crMUserRole)).getBody();
    List<ClientDto> clients = clientService.findClient(clientSearchDto, accessEntity);
    return ResponseEntity.ok(clients);
  }

  @Override
  public ResponseEntity<ClientLevelDto> getClientLevel(ClientIdDto clientIdDto, String crMUserRole,
      String crMUserName) {
    List<AccessDto> accessEntity = getAccess(
        commonMapper.getAccessRequest(crMUserRole)).getBody();
    ClientLevelDto clientLevel = clientService.getClientLevel(clientIdDto, accessEntity);
    return ResponseEntity.ok(clientLevel);
  }

  @Override
  public ResponseEntity<List<ContactDto>> getContact(ClientIdDto clientIdDto, String crMUserRole,
      String crMUserName) {
    List<AccessDto> accessEntity = getAccess(
        commonMapper.getAccessRequest(crMUserRole)).getBody();
    List<ContactDto> contacts = clientService.getContact(clientIdDto, accessEntity);
    return ResponseEntity.ok(contacts);
  }

  @Override
  public ResponseEntity<LoanPaymentDto> getLoanPayment(AccountNumberDto accountNumberDto,
      String crMUserRole, String crMUserName) {
    List<AccessDto> accessEntity = getAccess(
        commonMapper.getAccessRequest(crMUserRole)).getBody();
    LoanPaymentDto loanPayment = accountService.getLoanPayment(accountNumberDto, accessEntity);
    return ResponseEntity.ok(loanPayment);
  }

  @Override
  public ResponseEntity<ContactDto> saveContact(ContactDto contactDto, String crMUserRole,
      String crMUserName) {
    List<AccessDto> accessEntity = getAccess(
        commonMapper.getAccessRequest(crMUserRole)).getBody();
    ContactDto contact = clientService.saveContact(contactDto, accessEntity);
    return ResponseEntity.ok(contact);
  }

  @Override
  public ResponseEntity<Void> dummyGet() {
    return ResponseEntity.status(HttpStatus.OK).build();
  }
}
