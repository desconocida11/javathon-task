package ru.filit.mdma.repository;

import java.time.LocalDate;
import java.util.List;
import ru.filit.mdma.model.entity.Account;
import ru.filit.mdma.model.entity.AccountBalance;

/**
 * @author A.Khalitova 27-Nov-2021
 */
public interface AccountRepository {

  List<Account> findAccounts(String clientId);

  AccountBalance getBalanceByDate(String accountNumber, LocalDate date);

}
