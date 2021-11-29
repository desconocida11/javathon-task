package ru.filit.mdma.repository;

import java.time.LocalDate;
import java.util.List;
import ru.filit.mdma.model.entity.Operation;

/**
 * @author A.Khalitova 27-Nov-2021
 */
public interface OperationRepository {

  /**
   * @param accountNumber номер счета
   * @param quantity кол-во операций, если 0 - не ограничивать
   * @return операции по убыванию даты
   */
  List<Operation> getOperations(String accountNumber, int quantity);

  List<Operation> getOperationsByPeriod(LocalDate from, LocalDate to);
}
