package ru.filit.mdma.web.mapping;

import java.lang.annotation.Target;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import org.apache.commons.validator.routines.EmailValidator;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import ru.filit.mdma.model.entity.Client;
import ru.filit.mdma.model.entity.Contact;
import ru.filit.mdma.web.dto.ClientDto;
import ru.filit.mdma.web.dto.ClientSearchDto;
import ru.filit.mdma.web.dto.ContactDto;

/**
 * @author A.Khalitova 26-Nov-2021
 */
@Mapper
public abstract class DtoMapper {

  public static final DtoMapper INSTANCE = Mappers.getMapper(DtoMapper.class);

  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

  protected String fromInstant(Long timestamp) {
    if (timestamp == null) {
      return null;
    }
    Instant instant = Instant.ofEpochMilli(timestamp);
    LocalDate date = LocalDate.ofInstant(instant, ZoneId.of("UTC"));
    return date.format(FORMATTER);
  }

  // TODO search by birth date?
  protected Long toInstant(String date) {
    if (date == null) {
      return null;
    }
    LocalDate localDate = LocalDate.parse(date, FORMATTER);
    LocalDateTime localDateTime = LocalDateTime.of(localDate,
        LocalTime.of(0,0,0,0));
    return localDateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
  }

  @Mapping(target = "birthDate", expression = "java(fromInstant(client.getBirthDate()))")
  public abstract ClientDto clientToClientDto(Client client);

  @Mapping(target = "birthDate", expression = "java(toInstant(clientDto.getBirthDate()))")
  public abstract Client clientDtoToClient(ClientDto clientDto);

  @Mappings(value = {
      @Mapping(target = "birthDate",
          expression = "java(toInstant(clientSearchDto.getBirthDate()))")
  })
  public abstract Client clientSearchDtoToClient(ClientSearchDto clientSearchDto);

  public abstract ContactDto contactToContactDto(Contact contact);

  public abstract Contact contactDtoToContact(ContactDto contactDto);

  @AfterMapping
  protected void setShortcut(Contact contact, @MappingTarget ContactDto contactDto) {
    String value = contact.getValue();
    if (value == null) {
      return;
    }
    switch (contact.getType()) {
      case EMAIL -> {
        if (EmailValidator.getInstance().isValid(value)) {
          int fromIndex = value.lastIndexOf("@") - 1;
          contactDto.setShortcut(value.substring(fromIndex));
        }
      }
      case PHONE -> {
        if (value.length() > 4) {
          contactDto.setShortcut(value.substring(value.length() - 4));
        }
      }
    }
  }

  @AfterMapping
  protected void setPassport(ClientSearchDto clientSearchDto, @MappingTarget Client client) {
    if (clientSearchDto.getPassport() == null) {
      return;
    }
    String[] passport = clientSearchDto.getPassport().split(" ");
    client.setPassportSeries(passport[0]);
    client.setPassportNumber(passport[1]);
  }
}
