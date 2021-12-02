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
import org.springframework.web.server.ResponseStatusException;
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
import ru.filit.mdma.web.exception.DmServiceException;
import ru.filit.mdma.web.mapping.DtoMapper;

@AllArgsConstructor
@Service
@Slf4j
public class ClientServiceImpl implements ClientService {

  private final WebClient webClient;

  private final ObjectMapper objectMapper;

  @Override
  public ClientDto getClient(ClientIdDto clientIdDto) {
    ClientSearchDto clientSearchDto = DtoMapper.INSTANCE.idDtoToSearchDto(clientIdDto);
    List<ClientDto> client = findClient(clientSearchDto);
    if (client.size() != 1) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found");
    }
    ClientDto clientDto = client.get(0);

    List<ContactDto> contacts = getContact(clientIdDto);
    if (contacts != null && !contacts.isEmpty()) {
      clientDto.setContacts(contacts);
    }

    List<AccountDto> accounts = getAccount(clientIdDto);
    if (accounts != null && !accounts.isEmpty()) {
      clientDto.setAccounts(accounts);
    }
    return clientDto;
  }

  @Override
  public List<ClientDto> findClient(ClientSearchDto clientSearchDto) {
    if (Stream.of(clientSearchDto.getId(), clientSearchDto.getBirthDate(),
        clientSearchDto.getFirstname(), clientSearchDto.getLastname(),
        clientSearchDto.getPatronymic(), clientSearchDto.getInn(),
        clientSearchDto.getPassport())
        .allMatch(Objects::isNull)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND,
          "All fields are null");
    }
    final RequestBuilder<List<ClientDto>, ClientSearchDto> requestBuilder = new RequestBuilder<>();
    Mono<ResponseEntity<List<ClientDto>>> entity = requestBuilder
        .sendRequestWithResponseEntity("/client", clientSearchDto);
    final ResponseEntity<List<ClientDto>> responseEntity = entity.block();
    if (responseEntity == null || responseEntity.getStatusCode() != HttpStatus.OK) {
      throw new DmServiceException("Exception occurred in DM app on client request");
    }
    final List<ClientDto> clients = objectMapper
        .convertValue(responseEntity.getBody(), new TypeReference<>() {
        });
    return clients;
  }

  @Override
  public List<ContactDto> getContact(ClientIdDto clientIdDto) {
    final RequestBuilder<List<ContactDto>, ClientIdDto> requestBuilder = new RequestBuilder<>();
    Mono<ResponseEntity<List<ContactDto>>> entity = requestBuilder
        .sendRequestWithResponseEntity("/client/contact", clientIdDto);
    final ResponseEntity<List<ContactDto>> responseEntity = entity.block();
    if (responseEntity == null || responseEntity.getStatusCode() != HttpStatus.OK) {
      throw new DmServiceException("Exception occurred in DM app on contact request");
    }
    final List<ContactDto> contacts = objectMapper
        .convertValue(responseEntity.getBody(), new TypeReference<>() {
        });
    return contacts;
  }

  @Override
  public ClientLevelDto getClientLevel(ClientIdDto clientIdDto) {
    RequestBuilder<ClientLevelDto, ClientIdDto> requestBuilder = new RequestBuilder<>();
    Mono<ResponseEntity<ClientLevelDto>> entity = requestBuilder
        .sendRequestWithResponseEntity("/client/level", clientIdDto);
    ResponseEntity<ClientLevelDto> responseEntity = entity.block();
    if (responseEntity == null || responseEntity.getStatusCode() != HttpStatus.OK) {
      throw new DmServiceException("Exception occurred in DM app on client level request");
    }
    ClientLevelDto clientLevel = objectMapper
        .convertValue(responseEntity.getBody(), new TypeReference<>() {
        });
    return clientLevel;
  }

  @Override
  public List<AccountDto> getAccount(ClientIdDto clientIdDto) {
    final RequestBuilder<List<AccountDto>, ClientIdDto> requestBuilder = new RequestBuilder<>();
    Mono<ResponseEntity<List<AccountDto>>> entity = requestBuilder
        .sendRequestWithResponseEntity("/client/account", clientIdDto);
    final ResponseEntity<List<AccountDto>> responseEntity = entity.block();
    if (responseEntity == null || responseEntity.getStatusCode() != HttpStatus.OK) {
      throw new DmServiceException("Exception occurred in DM app on account request");
    }
    final List<AccountDto> accounts = objectMapper
        .convertValue(responseEntity.getBody(), new TypeReference<>() {
        });
    return accounts;
  }

  @Override
  public List<OperationDto> getAccountOperations(
      AccountNumberDto accountNumberDto) {
    OperationSearchDto operationSearchDto = DtoMapper.INSTANCE
        .accountNumberToOperationSearch(accountNumberDto);
    final RequestBuilder<List<OperationDto>, OperationSearchDto> requestBuilder =
        new RequestBuilder<>();
    Mono<ResponseEntity<List<OperationDto>>> entity = requestBuilder
        .sendRequestWithResponseEntity("/client/account/operation", operationSearchDto);
    final ResponseEntity<List<OperationDto>> responseEntity = entity.block();
    if (responseEntity == null || responseEntity.getStatusCode() != HttpStatus.OK) {
      throw new DmServiceException("Exception occurred in DM app");
    }
    final List<OperationDto> operations = objectMapper
        .convertValue(responseEntity.getBody(), new TypeReference<>() {
        });
    return operations;
  }

  @Override
  public ContactDto saveContact(ContactDto contactDto) {
    final RequestBuilder<ContactDto, ContactDto> requestBuilder = new RequestBuilder<>();
    Mono<ResponseEntity<ContactDto>> entity = requestBuilder
        .sendRequestWithResponseEntity("/client/contact/save", contactDto);
    ResponseEntity<ContactDto> responseEntity = entity.block();
    if (responseEntity == null || responseEntity.getStatusCode() != HttpStatus.OK) {
      throw new DmServiceException("Exception occurred in DM app");
    }
    ContactDto contact = objectMapper
        .convertValue(responseEntity.getBody(), new TypeReference<>() {
        });
    return contact;
  }

  @Override
  public LoanPaymentDto getLoanPayment(AccountNumberDto accountNumberDto) {
    final RequestBuilder<LoanPaymentDto, AccountNumberDto> requestBuilder = new RequestBuilder<>();
    Mono<ResponseEntity<LoanPaymentDto>> entity = requestBuilder
        .sendRequestWithResponseEntity("/client/account/loan-payment", accountNumberDto);
    ResponseEntity<LoanPaymentDto> responseEntity = entity.block();
    if (responseEntity == null || responseEntity.getStatusCode() != HttpStatus.OK) {
      throw new DmServiceException("Exception occurred in DM app");
    }
    LoanPaymentDto loanPaymentDto = objectMapper
        .convertValue(responseEntity.getBody(), new TypeReference<>() {
        });
    return loanPaymentDto;
  }

  private class RequestBuilder<T, B> {

    private Mono<ResponseEntity<T>> sendRequestWithResponseEntity(String uri, B body) {
      log.info("The request sent to {} with body: {}", uri, body.toString());
      return webClient
          .post()
          .uri(uri)
          .contentType(MediaType.APPLICATION_JSON)
          .bodyValue(body)
          .retrieve()
          .onStatus(HttpStatus::isError, response -> {
            log.error("response from DM app {}", response.statusCode());
            return Mono.error(new DmServiceException(
                "Exception " + response.statusCode() + " occurred on DM side"));
          })
          .toEntity(new ParameterizedTypeReference<>() {
          });
    }

    private T blockAndGetDto(Mono<T> entity) {
      return objectMapper.convertValue(entity.block(), new TypeReference<>() {
      });
    }

    private Mono<T> sendRequest(String uri, B body) {
      log.info("The request sent to {} with body: {}", uri, body.toString());
      return webClient
          .post()
          .uri(uri)
          .contentType(MediaType.APPLICATION_JSON)
          .bodyValue(body)
          .exchangeToMono(response -> {
            if (response.statusCode().equals(HttpStatus.OK)) {
              return response.bodyToMono(new ParameterizedTypeReference<T>() {
              });
            } else {
              return response.createException().flatMap(error -> {
                log.info("Text {}, status {}",
                    error.getResponseBodyAsString(), error.getStatusCode());
                return Mono.error(new DmServiceException(error.getResponseBodyAsString()));
              });
            }
          });
    }
  }
}
