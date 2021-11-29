package ru.filit.mdma.repository;

import java.util.List;
import ru.filit.mdma.model.entity.Client;

/**
 * @author A.Khalitova 26-Nov-2021
 */
public interface ClientRepository {

  /**
   * @param id клиента
   * @return Объект клиента или null, если клиент не найден
   */
  Client getClientById(String id);

  List<Client> findClientByParams(Client clientSearch);

}
