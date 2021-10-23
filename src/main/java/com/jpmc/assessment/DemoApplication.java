package com.jpmc.assessment;

import com.jpmc.assessment.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

@SpringBootApplication
public class DemoApplication {
  private static final Logger log = LoggerFactory.getLogger(DemoApplication.class);

  public static void main(String[] args) {
    SpringApplication.run(DemoApplication.class, args);
  }

  @Bean
  public CommandLineRunner loadCsv(BookService service) {
    return (args) -> {
      try (InputStream inputStream = getClass().getResourceAsStream("/books-repo.csv");
           BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
        service.upload(reader);
      }
      log.info("Repository populated..!!:");
    };
  }
}
