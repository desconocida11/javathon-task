package ru.filit.mdma.repository;

import java.util.List;
import ru.filit.mdma.model.entity.Contact;

/**
 * @author A.Khalitova 27-Nov-2021
 */
public interface ContactRepository {

  List<Contact> getContacts(String clientId);

  /**
   * @param contact контакт для сохранения/изменения
   * @return Контакт с id или null, если клиента не существует
   */
  Contact saveContact(Contact contact);

}
