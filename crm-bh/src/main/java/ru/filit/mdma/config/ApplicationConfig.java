package ru.filit.mdma.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Класс приложения Spring Boot.
 */
@SpringBootApplication(scanBasePackages = {"ru.filit.mdma"})
public class ApplicationConfig extends SpringBootServletInitializer {

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.sources(ApplicationConfig.class);
  }

  public static void main(String[] args) {
    SpringApplication.run(ApplicationConfig.class, args);
  }
}