package ru.filit.mdma.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * Конфигуратор Web интерфейса приложения.
 */
@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

  private final BeanFactory beanFactory;

  @Autowired
  public WebConfig(BeanFactory beanFactory) {
    this.beanFactory = beanFactory;
  }

  @Override
  public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
    configurer.enable();
  }

  @Override
  public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    converters.add(jacksonConverter());
  }

  private MappingJackson2HttpMessageConverter jacksonConverter() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

    MappingJackson2HttpMessageConverter jacksonConverter =
        new MappingJackson2HttpMessageConverter();
    jacksonConverter.setObjectMapper(mapper);
    return jacksonConverter;
  }
}