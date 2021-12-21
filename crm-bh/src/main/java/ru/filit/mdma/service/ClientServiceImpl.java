package ru.filit.mdma.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.ConnectException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import ru.filit.mdma.util.ServiceUnavailableException;
import ru.filit.mdma.web.dto.AccountDto;
import ru.filit.mdma.web.dto.AccountNumberDto;
import ru.filit.mdma.web.dto.ClientDto;
import ru.filit.mdma.web.dto.ClientIdDto;
import ru.filit.mdma.web.dto.ClientLevelDto;
import ru.filit.mdma.web.dto.ClientSearchDto;
import ru.filit.mdma.web.dto.ContactDto;
import ru.filit.mdma.web.dto.CurrentBalanceDto;
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

  private final AuthenticationService authentication;

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
      for (AccountDto account: accounts) {
        AccountNumberDto request = new AccountNumberDto();
        request.setAccountNumber(account.getNumber());
        account.setBalanceAmount(getAccountBalance(request).getBalanceAmount());
      }
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
    RequestBuilder<List<ClientDto>, ClientSearchDto> requestBuilder = new RequestBuilder<>();
    Mono<List<ClientDto>> entity = requestBuilder
        .sendRequest("/client", clientSearchDto)
        .onErrorMap(this::checkRootCause, t -> new ServiceUnavailableException());
    return objectMapper
        .convertValue(entity.block(), new TypeReference<>() {
        });
  }

  @Override
  public List<ContactDto> getContact(ClientIdDto clientIdDto) {
    RequestBuilder<List<ContactDto>, ClientIdDto> requestBuilder = new RequestBuilder<>();
    Mono<List<ContactDto>> entity = requestBuilder
        .sendRequest("/client/contact", clientIdDto)
        .onErrorMap(this::checkRootCause, t -> new ServiceUnavailableException());
    List<ContactDto> responseEntity = objectMapper
        .convertValue(entity.block(), new TypeReference<>() {
        });
    if (responseEntity == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND,
          "Null value");
    }
    return responseEntity;
  }

  @Override
  public ClientLevelDto getClientLevel(ClientIdDto clientIdDto) {
    RequestBuilder<ClientLevelDto, ClientIdDto> requestBuilder = new RequestBuilder<>();
    Mono<ClientLevelDto> entity = requestBuilder
        .sendRequest("/client/level", clientIdDto)
        .onErrorMap(this::checkRootCause, t -> new ServiceUnavailableException());
    ClientLevelDto responseEntity = objectMapper.convertValue(entity.block(),
        new TypeReference<>() {
        });
    if (responseEntity == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND,
          "Null value");
    }
    return responseEntity;
  }

  @Override
  public List<AccountDto> getAccount(ClientIdDto clientIdDto) {
    RequestBuilder<List<AccountDto>, ClientIdDto> requestBuilder = new RequestBuilder<>();
    Mono<List<AccountDto>> entity = requestBuilder
        .sendRequest("/client/account", clientIdDto)
        .onErrorMap(this::checkRootCause, t -> new ServiceUnavailableException());
    List<AccountDto> responseEntity = objectMapper
        .convertValue(entity.block(), new TypeReference<>() {
        });
    if (responseEntity == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND,
          "Null value");
    }
    return responseEntity;
  }

  @Override
  public List<OperationDto> getAccountOperations(
      AccountNumberDto accountNumberDto) {
    OperationSearchDto operationSearchDto = DtoMapper.INSTANCE
        .accountNumberToOperationSearch(accountNumberDto);
    RequestBuilder<List<OperationDto>, OperationSearchDto> requestBuilder =
        new RequestBuilder<>();
    Mono<List<OperationDto>> entity = requestBuilder
        .sendRequest("/client/account/operation", operationSearchDto)
        .onErrorMap(this::checkRootCause, t -> new ServiceUnavailableException());
    List<OperationDto> responseEntity = objectMapper
        .convertValue(entity.block(), new TypeReference<>() {
        });
    if (responseEntity == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND,
          "Null value");
    }
    return responseEntity;
  }

  @Override
  public ContactDto saveContact(ContactDto contactDto) {
    RequestBuilder<ContactDto, ContactDto> requestBuilder = new RequestBuilder<>();
    Mono<ContactDto> entity = requestBuilder
        .sendRequest("/client/contact/save", contactDto)
        .onErrorMap(this::checkRootCause, t -> new ServiceUnavailableException());
    ContactDto responseEntity = objectMapper
        .convertValue(entity.block(), new TypeReference<>() {
        });
    if (responseEntity == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND,
          "Null value");
    }
    return responseEntity;
  }

  @Override
  public LoanPaymentDto getLoanPayment(AccountNumberDto accountNumberDto) {
    RequestBuilder<LoanPaymentDto, AccountNumberDto> requestBuilder = new RequestBuilder<>();
    Mono<LoanPaymentDto> entity = requestBuilder
        .sendRequest("/client/account/loan-payment", accountNumberDto)
        .onErrorMap(this::checkRootCause, t -> new ServiceUnavailableException());
    LoanPaymentDto responseEntity = objectMapper
        .convertValue(entity.block(), new TypeReference<>() {
        });
    if (responseEntity == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND,
          "Null value");
    }
    return responseEntity;
  }

  private CurrentBalanceDto getAccountBalance(AccountNumberDto accountNumberDto) {
    RequestBuilder<CurrentBalanceDto, AccountNumberDto> requestBuilder = new RequestBuilder<>();
    Mono<CurrentBalanceDto> entity = requestBuilder
        .sendRequest("/client/account/balance", accountNumberDto)
        .onErrorMap(this::checkRootCause, t -> new ServiceUnavailableException());
    CurrentBalanceDto responseEntity = objectMapper
        .convertValue(entity.block(), new TypeReference<>() {
        });
    if (responseEntity == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND,
          "Null value");
    }
    return responseEntity;
  }

  private boolean checkRootCause(Throwable t) {
    Throwable rootCause = t;
    while (rootCause.getCause() != null && rootCause.getCause() != rootCause &&
        !rootCause.getClass().equals(ConnectException.class)) {
      rootCause = rootCause.getCause();
    }
    return rootCause.getClass().equals(ConnectException.class);
  }

  private class RequestBuilder<T, B> {

    private Mono<T> sendRequest(String uri, B body) {
      log.info("The request sent to {} with body: {}", uri, body.toString());
      return webClient
          .post()
          .uri(uri)
          .contentType(MediaType.APPLICATION_JSON)
          .header("CRM-User-Role", authentication.getAuthRole())
          .header("CRM-User-Name", authentication.getAuthName())
          .bodyValue(body)
          .exchangeToMono(response -> {
            if (response.statusCode().equals(HttpStatus.OK)) {
              return response.bodyToMono(new ParameterizedTypeReference<>() {
              });
            } else {
              return response.createException().flatMap(error -> {
                log.info("Text {}, status {}",
                    error.getResponseBodyAsString(), error.getStatusCode());
                return Mono.error(
                    new ResponseStatusException(error.getStatusCode(), "Exception occurred on Intranet side"));
              });
            }
          });
    }
  }
}
