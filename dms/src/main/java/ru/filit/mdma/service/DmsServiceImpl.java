package ru.filit.mdma.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import ru.filit.mdma.model.cache.TokenService;
import ru.filit.mdma.web.dto.AccessDto;
import ru.filit.mdma.web.dto.AccessRequestDto;
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

/**
 * @author A.Khalitova 03-Dec-2021
 */
@Service
@AllArgsConstructor
@Slf4j
public class DmsServiceImpl implements ClientService, AccountService, AccessService {

  private final WebClient webClient;

  private final ObjectMapper objectMapper;

  private final TokenService tokenService;

  @Override
  public List<AccessDto> getAccess(AccessRequestDto accessRequestDto) {
    if (accessRequestDto.getRole() == null) {
      throw new ResponseStatusException(
          HttpStatus.FORBIDDEN, "Role is not determined");
    }
    RequestBuilder<List<AccessDto>, AccessRequestDto> requestBuilder = new RequestBuilder<>();
    Mono<List<AccessDto>> entity = requestBuilder
        .sendRequest("/access", accessRequestDto);
    List<AccessDto> accessDtos =  objectMapper.convertValue(entity.block(), new TypeReference<>() {
    });
    if (accessDtos == null) {
      throw new ResponseStatusException(
          HttpStatus.INTERNAL_SERVER_ERROR, "Role is not determined");
    }
    return accessDtos;
  }

  @Override
  public List<AccountDto> getAccount(ClientIdDto clientIdDto, List<AccessDto> access) {
    tokenService.detokenizeObject(clientIdDto);
    RequestBuilder<List<AccountDto>, ClientIdDto> requestBuilder = new RequestBuilder<>();
    Mono<List<AccountDto>> entity = requestBuilder
        .sendRequest("/client/account", clientIdDto);
    List<AccountDto> accountDtos = objectMapper.convertValue(entity.block(), new TypeReference<>() {
    });
    if (accountDtos == null) {
      throw new ResponseStatusException(
          HttpStatus.NOT_FOUND, "Null account");
    }
    tokenService.tokenizeObject(accountDtos, access, "account");
    return accountDtos;
  }

  @Override
  public CurrentBalanceDto getAccountBalance(AccountNumberDto accountNumberDto,
      List<AccessDto> access) {
    tokenService.detokenizeObject(accountNumberDto);
    RequestBuilder<CurrentBalanceDto, AccountNumberDto> requestBuilder = new RequestBuilder<>();
    Mono<CurrentBalanceDto> entity = requestBuilder
        .sendRequest("/client/account/balance", accountNumberDto);
    CurrentBalanceDto currentBalance = objectMapper.convertValue(entity.block(),
        new TypeReference<>() {
        });
    if (currentBalance == null) {
      throw new ResponseStatusException(
          HttpStatus.NOT_FOUND, "Null current balance");
    }
    tokenService.tokenizeObject(currentBalance, access, "currentBalance");
    return currentBalance;
  }

  @Override
  public List<OperationDto> getAccountOperations(OperationSearchDto operationSearchDto,
      List<AccessDto> access) {
    tokenService.detokenizeObject(operationSearchDto);
    RequestBuilder<List<OperationDto>, OperationSearchDto> requestBuilder = new RequestBuilder<>();
    Mono<List<OperationDto>> entity = requestBuilder
        .sendRequest("/client/account/operation", operationSearchDto);
    List<OperationDto> operations = objectMapper.convertValue(entity.block(),
        new TypeReference<>() {
        });
    if (operations == null) {
      throw new ResponseStatusException(
          HttpStatus.NOT_FOUND, "Null operation");
    }
    tokenService.tokenizeObject(operations, access, "operation");
    return operations;
  }

  @Override
  public List<ClientDto> findClient(ClientSearchDto clientSearchDto, List<AccessDto> access) {
    tokenService.detokenizeObject(clientSearchDto);
    RequestBuilder<List<ClientDto>, ClientSearchDto> requestBuilder = new RequestBuilder<>();
    Mono<List<ClientDto>> entity = requestBuilder
        .sendRequest("/client", clientSearchDto);
    List<ClientDto> clientDtos = objectMapper
        .convertValue(entity.block(), new TypeReference<>() {
        });
    if (clientDtos == null) {
      throw new ResponseStatusException(
          HttpStatus.NOT_FOUND, "Null client");
    }
    tokenService.tokenizeObject(clientDtos, access, "client");
    return clientDtos;
  }

  @Override
  public ClientLevelDto getClientLevel(ClientIdDto clientIdDto, List<AccessDto> access) {
    tokenService.detokenizeObject(clientIdDto);
    RequestBuilder<ClientLevelDto, ClientIdDto> requestBuilder = new RequestBuilder<>();
    Mono<ClientLevelDto> entity = requestBuilder
        .sendRequest("/client/level", clientIdDto);
    ClientLevelDto clientLevelDto = objectMapper
        .convertValue(entity.block(), new TypeReference<>() {
        });
    if (clientLevelDto == null) {
      throw new ResponseStatusException(
          HttpStatus.NOT_FOUND, "Null client level");
    }
    tokenService.tokenizeObject(clientLevelDto, access, "clientLevel");
    return clientLevelDto;
  }

  @Override
  public List<ContactDto> getContact(ClientIdDto clientIdDto, List<AccessDto> access) {
    tokenService.detokenizeObject(clientIdDto);
    RequestBuilder<List<ContactDto>, ClientIdDto> requestBuilder = new RequestBuilder<>();
    Mono<List<ContactDto>> entity = requestBuilder
        .sendRequest("/client/contact", clientIdDto);
    List<ContactDto> contacts = objectMapper
        .convertValue(entity.block(), new TypeReference<>() {
        });
    if (contacts == null) {
      throw new ResponseStatusException(
          HttpStatus.NOT_FOUND, "Null contact");
    }
    tokenService.tokenizeObject(contacts, access, "contact");
    return contacts;
  }

  @Override
  public ContactDto saveContact(ContactDto contactDto, List<AccessDto> access) {
    tokenService.detokenizeObject(contactDto);
    RequestBuilder<ContactDto, ContactDto> requestBuilder = new RequestBuilder<>();
    Mono<ContactDto> entity = requestBuilder
        .sendRequest("/client/contact/save", contactDto);
    ContactDto contact = objectMapper
        .convertValue(entity.block(), new TypeReference<>() {
        });
    if (contact == null) {
      throw new ResponseStatusException(
          HttpStatus.NOT_FOUND, "Null contact");
    }
    tokenService.tokenizeObject(contact, access, "contact");
    return contact;
  }

  @Override
  public LoanPaymentDto getLoanPayment(AccountNumberDto accountNumberDto, List<AccessDto> access) {
    tokenService.detokenizeObject(accountNumberDto);
    RequestBuilder<LoanPaymentDto, AccountNumberDto> requestBuilder = new RequestBuilder<>();
    Mono<LoanPaymentDto> entity = requestBuilder
        .sendRequest("/client/account/loan-payment", accountNumberDto);
    LoanPaymentDto loanPayment = objectMapper
        .convertValue(entity.block(), new TypeReference<>() {
        });
    if (loanPayment == null) {
      throw new ResponseStatusException(
          HttpStatus.NOT_FOUND, "Null loan payment");
    }
    tokenService.tokenizeObject(loanPayment, access, "loanPayment");
    return loanPayment;
  }

  private class RequestBuilder<T, B> {

    private Mono<T> sendRequest(String uri, B body) {
      log.info("The request sent to {} with body: {}", uri, body.toString());
      return webClient
          .post()
          .uri(uri)
          .contentType(MediaType.APPLICATION_JSON)
          .bodyValue(body)
          .exchangeToMono(response -> {
            if (response.statusCode().equals(HttpStatus.OK)) {
              return response.bodyToMono(new ParameterizedTypeReference<>() {
              });
            } else {
              return response.createException().flatMap(error -> {
                log.info("Text {}, status {}, ex {}",
                    error.getResponseBodyAsString(), error.getStatusCode(),
                    error.getLocalizedMessage());
                log.info("Text {}", error.toString());
                return Mono.error(
                    new ResponseStatusException(error.getStatusCode(), error.getResponseBodyAsString()));
              });
            }
          });
    }
  }
}
