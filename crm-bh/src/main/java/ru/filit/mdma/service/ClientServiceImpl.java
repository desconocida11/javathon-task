package ru.filit.mdma.service;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
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

@AllArgsConstructor
@Service
public class ClientServiceImpl implements ClientService {

  private final WebClient webClient;

  @Override
  public ResponseEntity<List<ClientDto>> findClient(ClientSearchDto clientSearchDto) {
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
      OperationSearchDto operationSearchDto) {
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
