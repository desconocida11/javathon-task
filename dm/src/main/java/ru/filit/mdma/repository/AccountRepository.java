package ru.filit.mdma.repository;

import java.util.List;
import ru.filit.mdma.model.entity.Account;

/**
 * @author A.Khalitova 27-Nov-2021
 */
public interface AccountRepository {

  List<Account> findAccounts(String clientId);

  Account getAccount(String accountNumber);

}
