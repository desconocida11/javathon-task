package ru.filit.mdma.repository.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import ru.filit.mdma.model.entity.Client;
import ru.filit.mdma.repository.ClientRepository;
import ru.filit.mdma.service.EntityRepo;
import ru.filit.mdma.web.exception.ClientNotFoundException;

/**
 * @author A.Khalitova 26-Nov-2021
 */
@Repository
public class ClientRepositoryImpl implements ClientRepository {

  @Value(value = "clients.yml")
  private String fileName;

  @Value(value = "${dm.repo.location}")
  private String filePath;

  private final EntityRepo entityRepo;

  private final Map<String, Client> clients = new ConcurrentHashMap<>();

  public ClientRepositoryImpl(EntityRepo entityRepo) {
    this.entityRepo = entityRepo;
  }

  @PostConstruct
  public void init() {
    final List<Client> clientList =
        entityRepo.readList(getFile(), new TypeReference<>() {
        });
    clientList.forEach(client -> clients.putIfAbsent(client.getId(), client));
  }

  @Override
  public Client getClientById(String id) {
    Client client = clients.get(id);
    if (client == null) {
      throw new ClientNotFoundException("Клиент с данным id не существует");
    }
    return client;
  }

  @Override
  public List<Client> findClientByParams(Client clientSearch) {
    Predicate<Client> idPredicate = client -> clientSearch.getId() == null
        || client.getId().equals(clientSearch.getId());
    Predicate<Client> firstnamePredicate = client -> clientSearch.getFirstname() == null
        || client.getFirstname().equals(clientSearch.getFirstname());
    Predicate<Client> lastnamePredicate = client -> clientSearch.getLastname() == null
        || client.getLastname().equals(clientSearch.getLastname());
    Predicate<Client> patronymicPredicate = client -> clientSearch.getPatronymic() == null
        || client.getPatronymic().equals(clientSearch.getPatronymic());
    Predicate<Client> innPredicate = client -> clientSearch.getInn() == null
        || client.getInn().equals(clientSearch.getInn());
    Predicate<Client> passportnumberPredicate = client -> clientSearch.getPassportNumber() == null
        || client.getPassportNumber().equals(clientSearch.getPassportNumber());
    Predicate<Client> passportseriesPredicate = client -> clientSearch.getPassportSeries() == null
        || client.getPassportSeries().equals(clientSearch.getPassportSeries());

    Predicate<Client> birthdatePredicate = client -> clientSearch.getBirthDate() == null
        || getLongForStartOfTheDay(client.getBirthDate()).equals(clientSearch.getBirthDate());

    return clients.values().stream()
        .filter(client -> {
          final Predicate<Client> result = idPredicate.and(firstnamePredicate.and(lastnamePredicate
              .and(patronymicPredicate.and(birthdatePredicate.and(innPredicate
                  .and(passportnumberPredicate.and(passportseriesPredicate)))))));
          return result.test(client);
        }).collect(Collectors.toList());
  }

  private Long getLongForStartOfTheDay(Long timestamp) {
    Instant instant = Instant.ofEpochMilli(timestamp);
    LocalDate date = LocalDate.ofInstant(instant, ZoneId.of("UTC"));
    LocalDateTime dateTime = LocalDateTime.of(date, LocalTime.of(0, 0, 0, 0));
    return dateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
  }

  private String getFile() {
    return filePath + fileName;
  }
}
