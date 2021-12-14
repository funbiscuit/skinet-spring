package com.example.skinet;

import com.example.skinet.service.JsonDbImporter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SkinetApplication {

    public static void main(String[] args) {
        SpringApplication.run(SkinetApplication.class, args);
    }

    @Bean
    ObjectMapper jsonMapper() {
        return new ObjectMapper();
    }

    @Bean
    public CommandLineRunner dbInitializer(JsonDbImporter jsonDbImporter) {
        return args -> jsonDbImporter.importData();
    }
}
