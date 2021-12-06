package ru.filit.mdma.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Класс приложения Spring Boot.
 */
@SpringBootApplication(scanBasePackages = {"ru.filit.mdma"})
public class DmsApplication extends SpringBootServletInitializer {

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.sources(DmsApplication.class);
  }

  public static void main(String[] args) {
    SpringApplication.run(DmsApplication.class, args);
  }
}