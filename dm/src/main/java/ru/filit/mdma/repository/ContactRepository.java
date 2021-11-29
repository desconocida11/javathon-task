package ru.filit.mdma.repository;

import java.util.List;
import ru.filit.mdma.model.entity.Contact;

/**
 * @author A.Khalitova 27-Nov-2021
 */
public interface ContactRepository {

  List<Contact> getContacts(String clientId);

  Contact saveContact(Contact contact);

}
