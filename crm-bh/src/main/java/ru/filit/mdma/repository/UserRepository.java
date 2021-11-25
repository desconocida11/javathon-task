package ru.filit.mdma.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import ru.filit.mdma.model.entity.User;
import ru.filit.mdma.util.EntityRepo;

/**
 * @author A.Khalitova 25-Nov-2021
 */
@Repository
public class UserRepository {

  @Getter
  private final List<User> users = new ArrayList<>();

  private final EntityRepo entityRepo;

  @Value(value = "classpath:db/users.yml")
  private String filePath;

  @Autowired
  public UserRepository(EntityRepo entityRepo) {
    this.entityRepo = entityRepo;
  }

  @PostConstruct
  public void init() {
    users.addAll(entityRepo.readList(filePath, new TypeReference<>() {
    }));
  }
}
