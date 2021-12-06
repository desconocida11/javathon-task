package ru.filit.mdma.service;

import java.util.List;
import ru.filit.mdma.web.dto.AccessDto;
import ru.filit.mdma.web.dto.AccountDto;
import ru.filit.mdma.web.dto.AccountNumberDto;
import ru.filit.mdma.web.dto.ClientIdDto;
import ru.filit.mdma.web.dto.CurrentBalanceDto;
import ru.filit.mdma.web.dto.LoanPaymentDto;
import ru.filit.mdma.web.dto.OperationDto;
import ru.filit.mdma.web.dto.OperationSearchDto;

/**
 * @author A.Khalitova 05-Dec-2021
 */
public interface AccountService {

  List<AccountDto> getAccount(ClientIdDto clientIdDto, List<AccessDto> access);

  CurrentBalanceDto getAccountBalance(AccountNumberDto accountNumberDto, List<AccessDto> access);

  List<OperationDto> getAccountOperations(OperationSearchDto operationSearchDto,
      List<AccessDto> access);

  LoanPaymentDto getLoanPayment(AccountNumberDto accountNumberDto, List<AccessDto> access);
}
