package ru.filit.mdma.repository;

import java.util.List;
import ru.filit.mdma.model.entity.Access;

/**
 * @author A.Khalitova 03-Dec-2021
 */
public interface AccessRepository {

  List<Access> getAccessForRole(String role, String version);

}
