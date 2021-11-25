package ru.filit.mdma.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfiguration {

  @Value("${system.element.dm.host}")
  private String baseHost;

  @Value("${system.element.dm.port}")
  private String basePort;

  @Bean
  public WebClient webClientWithTimeout() {
    // TODO get rid of constants
    return WebClient.builder()
        .baseUrl("http://" + baseHost + ":" + basePort + "/dm")
        .build();
  }
}