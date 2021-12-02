package ru.filit.mdma.repository.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;
import ru.filit.mdma.model.entity.Contact;
import ru.filit.mdma.repository.ContactRepository;
import ru.filit.mdma.service.EntityRepo;
import ru.filit.mdma.web.exception.ClientNotFoundException;

/**
 * @author A.Khalitova 27-Nov-2021
 */
@Repository
public class ContactRepositoryImpl implements ContactRepository {

  @Value(value = "contacts.yml")
  private String fileName;

  @Value(value = "${dm.repo.location}")
  private String filePath;

  private final EntityRepo entityRepo;

  // TODO is value appending/deleting thread-safe?
  private final Map<String, List<Contact>> contacts = new ConcurrentHashMap<>();

  public ContactRepositoryImpl(EntityRepo entityRepo) {
    this.entityRepo = entityRepo;
  }

  @PostConstruct
  public void init() {
    final List<Contact> contactList =
        entityRepo.readList(getFile(), new TypeReference<List<Contact>>() {
        });

    for (Contact contact : contactList) {
      contacts.computeIfAbsent(contact.getClientId(), v -> new CopyOnWriteArrayList<>())
          .add(contact);
    }
  }

  @Override
  public List<Contact> getContacts(String clientId) {
    return contacts.get(clientId);
  }

  @Override
  public synchronized Contact saveContact(Contact contact) {
    String clientId = contact.getClientId();
    if (!contacts.containsKey(clientId)) {
      throw new ClientNotFoundException("Client not found");
    }
    List<Contact> contactsByClientId = contacts.get(clientId);
    if (contact.getId() == null) {
      contact.setId(getGeneratedId());
    } else {
      contactsByClientId.removeIf(c -> c.getId().equals(contact.getId()));
    }
    contactsByClientId.add(contact);
    try {
      entityRepo.clearContents(getFile());
    } catch (FileNotFoundException e) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
          "Error occurred while saving the contact");
    }
    entityRepo.writeList(getFile(), contacts.values()
        .stream()
        .flatMap(Collection::stream)
        .toList());
    return contact;
  }

  private String getGeneratedId() {
    return UUID.randomUUID().toString();
  }

  private String getFile() {
    return filePath + fileName;
  }
}
