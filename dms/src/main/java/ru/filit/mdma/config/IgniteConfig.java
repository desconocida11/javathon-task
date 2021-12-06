package ru.filit.mdma.config;

import java.util.Arrays;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.spi.communication.tcp.TcpCommunicationSpi;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;
import org.apache.ignite.springframework.boot.autoconfigure.IgniteConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IgniteConfig {

  @Bean
  public IgniteConfigurer nodeConfigurer() {
    return cfg -> {
      //Setting some property.
      //Other will come from `application.yml`
      cfg.setIgniteInstanceName("igniteInstance");
      CacheConfiguration<String, String> cacheCfg = new CacheConfiguration<>();
      cacheCfg.setName("cache");
      // TODO need of Expiry policy?
      cacheCfg.setExpiryPolicyFactory(CreatedExpiryPolicy.factoryOf(Duration.THIRTY_MINUTES));

      TcpDiscoverySpi spi = new TcpDiscoverySpi();
      TcpDiscoveryVmIpFinder ipFinder = new TcpDiscoveryVmIpFinder();

      ipFinder.setAddresses(Arrays.asList("127.0.0.1", "127.0.0.1:47500..47509"));
      spi.setIpFinder(ipFinder);

      // TODO also this warn msg while debugging
      // https://apacheignite.readme.io/docs/jvm-and-system-tuning
      // WARN  org.apache.ignite.internal.IgniteKernal%igniteInstance :
      // Possible too long JVM pause: 5897 milliseconds.

      // TODO check this
      //Message queue limit is set to 0 which may lead to potential OOMEs
      // when running cache operations in FULL_ASYNC or PRIMARY_SYNC modes
      // due to message queues growth on sender and receiver sides.
      TcpCommunicationSpi tcpCommunicationSPI = new TcpCommunicationSpi();
      tcpCommunicationSPI.setMessageQueueLimit(1024);

      cfg.setCommunicationSpi(tcpCommunicationSPI);
      cfg.setDiscoverySpi(spi);
      cfg.setCacheConfiguration(cacheCfg);
    };
  }
}