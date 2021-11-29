package ru.filit.mdma.service;

import java.util.List;
import org.springframework.stereotype.Service;
import ru.filit.mdma.model.entity.Contact;
import ru.filit.mdma.repository.ContactRepository;
import ru.filit.mdma.web.dto.ClientIdDto;
import ru.filit.mdma.web.dto.ContactDto;
import ru.filit.mdma.web.exception.ClientNotFoundException;
import ru.filit.mdma.web.exception.InvalidDataException;
import ru.filit.mdma.web.mapping.DtoMapper;

/**
 * @author A.Khalitova 27-Nov-2021
 */
@Service
public class ContactService {

  private final ContactRepository contactRepository;

  public ContactService(ContactRepository contactRepository) {
    this.contactRepository = contactRepository;
  }

  public List<ContactDto> getContact(ClientIdDto clientIdDto) {
    String id = clientIdDto.getId();
    if (id == null || id.isBlank()) {
      throw new InvalidDataException("Отсутствует идентификатор клиента");
    }
    final List<Contact> contacts = contactRepository.getContacts(id);
    return contacts.stream()
        .map(DtoMapper.INSTANCE::contactToContactDto)
        .toList();
  }

  public ContactDto saveContact(ContactDto contactDto) {
    if (contactDto.getClientId() == null || contactDto.getClientId().isBlank()) {
      throw new InvalidDataException("Отсутствует идентификатор клиента");
    }
    Contact savedContact = contactRepository.saveContact(
        DtoMapper.INSTANCE.contactDtoToContact(contactDto));
    if (savedContact == null) {
      throw new ClientNotFoundException("Клиент не найден");
    }
    return DtoMapper.INSTANCE.contactToContactDto(savedContact);
  }
}
