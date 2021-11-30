package ru.filit.mdma.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.filit.mdma.web.dto.AccountDto;
import ru.filit.mdma.web.dto.AccountNumberDto;
import ru.filit.mdma.web.dto.ClientDto;
import ru.filit.mdma.web.dto.ClientIdDto;
import ru.filit.mdma.web.dto.ClientLevelDto;
import ru.filit.mdma.web.dto.ClientSearchDto;
import ru.filit.mdma.web.dto.ContactDto;
import ru.filit.mdma.web.dto.LoanPaymentDto;
import ru.filit.mdma.web.dto.OperationDto;
import ru.filit.mdma.web.dto.OperationSearchDto;
import ru.filit.mdma.web.mapping.DtoMapper;

@AllArgsConstructor
@Service
@Slf4j
public class ClientServiceImpl implements ClientService {

  private final WebClient webClient;

  private final ObjectMapper objectMapper;

  @Override
  public ResponseEntity<ClientDto> getClient(ClientIdDto clientIdDto) {
    ClientSearchDto clientSearchDto = DtoMapper.INSTANCE.idDtoToSearchDto(clientIdDto);
    ResponseEntity<List<ClientDto>> client = findClient(clientSearchDto);

    if (client.getStatusCode() != HttpStatus.OK
        || client.getBody() == null || client.getBody().size() != 1) {
      return ResponseEntity.status(client.getStatusCode()).build();
    }
    ClientDto clientDto = objectMapper.convertValue(client.getBody().get(0),
        new TypeReference<>() {
        });

    // TODO refactor: type erasure issue, LinkedHashMap to POJO in response body
    final ResponseEntity<List<ContactDto>> contact = getContact(clientIdDto);
    if (contact.getStatusCode().equals(HttpStatus.OK)
        && contact.getBody() != null) {
      List<ContactDto> contacts = objectMapper.convertValue(contact.getBody(),
          new TypeReference<>() {
          });
      clientDto.setContacts(contacts);
    }
    final ResponseEntity<List<AccountDto>> account = getAccount(clientIdDto);
    if (account.getStatusCode().equals(HttpStatus.OK)
        && account.getBody() != null) {
      List<AccountDto> accounts = objectMapper.convertValue(account.getBody(),
          new TypeReference<>() {
          });
      clientDto.setAccounts(accounts);
    }
    return ResponseEntity.status(HttpStatus.OK).body(clientDto);
  }

  @Override
  public ResponseEntity<List<ClientDto>> findClient(ClientSearchDto clientSearchDto) {
    // TODO message if all fields are null?
    if (Stream.of(clientSearchDto.getId(), clientSearchDto.getBirthDate(),
        clientSearchDto.getFirstname(), clientSearchDto.getLastname(),
        clientSearchDto.getPatronymic(), clientSearchDto.getInn(),
        clientSearchDto.getPassport())
        .allMatch(Objects::isNull)) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    final Mono<ResponseEntity<List<ClientDto>>> entity =
        new RequestBuilder<List<ClientDto>, ClientSearchDto>()
            .sendRequest("/client", clientSearchDto);
    return entity.block();
  }

  @Override
  public ResponseEntity<List<ContactDto>> getContact(ClientIdDto clientIdDto) {
    final Mono<ResponseEntity<List<ContactDto>>> entity =
        new RequestBuilder<List<ContactDto>, ClientIdDto>()
            .sendRequest("/client/contact", clientIdDto);
    return entity.block();
  }

  @Override
  public ResponseEntity<ClientLevelDto> getClientLevel(ClientIdDto clientIdDto) {
    final Mono<ResponseEntity<ClientLevelDto>> entity =
        new RequestBuilder<ClientLevelDto, ClientIdDto>()
            .sendRequest("/client/level", clientIdDto);
    return entity.block();
  }

  @Override
  public ResponseEntity<List<AccountDto>> getAccount(ClientIdDto clientIdDto) {
    final Mono<ResponseEntity<List<AccountDto>>> entity =
        new RequestBuilder<List<AccountDto>, ClientIdDto>()
            .sendRequest("/client/account", clientIdDto);
    return entity.block();
  }

  @Override
  public ResponseEntity<List<OperationDto>> getAccountOperations(
      AccountNumberDto accountNumberDto) {
    OperationSearchDto operationSearchDto = DtoMapper.INSTANCE
        .accountNumberToOperationSearch(accountNumberDto);
    final Mono<ResponseEntity<List<OperationDto>>> entity =
        new RequestBuilder<List<OperationDto>, OperationSearchDto>()
            .sendRequest("/client/account/operation", operationSearchDto);
    return entity.block();
  }

  @Override
  public ResponseEntity<ContactDto> saveContact(ContactDto contactDto) {
    final Mono<ResponseEntity<ContactDto>> entity = new RequestBuilder<ContactDto, ContactDto>()
        .sendRequest("/client/contact/save", contactDto);
    return entity.block();
  }

  @Override
  public ResponseEntity<LoanPaymentDto> getLoanPayment(AccountNumberDto accountNumberDto) {
    final Mono<ResponseEntity<LoanPaymentDto>> entity =
        new RequestBuilder<LoanPaymentDto, AccountNumberDto>()
            .sendRequest("/client/account/loan-payment", accountNumberDto);
    return entity.block();
  }

  private class RequestBuilder<T, B> {

    private Mono<ResponseEntity<T>> sendRequest(String uri, B body) {
      return webClient
          .post()
          .uri(uri)
          .contentType(MediaType.APPLICATION_JSON)
          .bodyValue(body)
          .retrieve()
          .toEntity(new ParameterizedTypeReference<>() {
          });
    }
  }
}
