package ru.filit.mdma.model.cache;

/**
 * @author A.Khalitova 06-Dec-2021
 */
public interface SensitiveDataCache {

  boolean put(String key, String value);

  String read(String key);
}
