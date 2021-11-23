package ru.filit.mdma.web.controller;

import org.springframework.http.ResponseEntity;
import ru.filit.mdma.web.dto.*;

import java.util.List;


/**
 * Контроллер действий с клиентами.
 */
public class ClientApiImpl implements ClientApi {
    @Override
    public ResponseEntity<List<AccountDto>> getAccount(ClientIdDto clientIdDto) {
        return null;
    }

    @Override
    public ResponseEntity<CurrentBalanceDto> getAccountBalance(AccountNumberDto accountNumberDto) {
        return null;
    }

    @Override
    public ResponseEntity<List<OperationDto>> getAccountOperations(OperationSearchDto operationSearchDto) {
        return null;
    }

    @Override
    public ResponseEntity<List<ClientDto>> getClient(ClientSearchDto clientSearchDto) {
        return null;
    }

    @Override
    public ResponseEntity<ClientLevelDto> getClientLevel(ClientIdDto clientIdDto) {
        return null;
    }

    @Override
    public ResponseEntity<List<ContactDto>> getContact(ClientIdDto clientIdDto) {
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
