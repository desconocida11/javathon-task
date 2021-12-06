package ru.filit.mdma.service;

import java.util.List;
import ru.filit.mdma.web.dto.AccessDto;
import ru.filit.mdma.web.dto.ClientDto;
import ru.filit.mdma.web.dto.ClientIdDto;
import ru.filit.mdma.web.dto.ClientLevelDto;
import ru.filit.mdma.web.dto.ClientSearchDto;
import ru.filit.mdma.web.dto.ContactDto;

/**
 * @author A.Khalitova 03-Dec-2021
 */
public interface ClientService {

  List<ClientDto> findClient(ClientSearchDto clientSearchDto, List<AccessDto> access);

  ClientLevelDto getClientLevel(ClientIdDto clientIdDto, List<AccessDto> access);

  List<ContactDto> getContact(ClientIdDto clientIdDto, List<AccessDto> access);

  ContactDto saveContact(ContactDto contactDto, List<AccessDto> access);

}
