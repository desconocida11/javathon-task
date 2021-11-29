package ru.filit.mdma.service;

import java.util.List;
import org.springframework.http.ResponseEntity;
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

public interface ClientService {

  ResponseEntity<List<ClientDto>> findClient(ClientSearchDto clientSearchDto);

  ResponseEntity<List<ClientDto>> findClientById(ClientIdDto clientIdDto);

  ResponseEntity<List<ContactDto>> getContact(ClientIdDto clientIdDto);

  ResponseEntity<ClientLevelDto> getClientLevel(ClientIdDto clientIdDto);

  ResponseEntity<List<AccountDto>> getAccount(ClientIdDto clientIdDto);

  ResponseEntity<List<OperationDto>> getAccountOperations(OperationSearchDto operationSearchDto);

  ResponseEntity<ContactDto> saveContact(ContactDto contactDto);

  ResponseEntity<LoanPaymentDto> getLoanPayment(AccountNumberDto accountNumberDto);
}
