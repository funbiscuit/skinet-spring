package com.example.skinet;

import com.example.skinet.config.AppConfigProperties;
import com.example.skinet.core.entity.Product;
import com.example.skinet.core.entity.ProductDTO;
import com.example.skinet.service.JsonDbImporter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisKeyValueAdapter.EnableKeyspaceEvents;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@SpringBootApplication
// enable listening for keyspace events so entry keys are automatically removed
// from index when original key expires
@EnableRedisRepositories(enableKeyspaceEvents = EnableKeyspaceEvents.ON_STARTUP)
@ConfigurationPropertiesScan("com.example.skinet.config")
public class SkinetApplication {

    public static void main(String[] args) {
        SpringApplication.run(SkinetApplication.class, args);
    }

    @Bean
    ObjectMapper jsonMapper() {
        return new ObjectMapper();
    }

    @Bean
    ModelMapper modelMapper(AppConfigProperties configProperties) {
        ModelMapper mapper = new ModelMapper();
        Converter<String, String> urlToGlobal = ctx -> configProperties.getRootUrl() + ctx.getSource();

        TypeMap<Product, ProductDTO> typeMap = mapper.createTypeMap(Product.class, ProductDTO.class);
        // this could be done by just overriding toSting, but in case we need
        // different conversions in different places, do it this way
        typeMap.addMapping(p -> p.getBrand().getName(), ProductDTO::setBrand);
        typeMap.addMapping(p -> p.getType().getName(), ProductDTO::setType);

        typeMap.addMappings(m -> m.using(urlToGlobal)
                .map(Product::getPictureUrl, ProductDTO::setPictureUrl));

        return mapper;
    }

    @Bean
    public CommandLineRunner dbInitializer(JsonDbImporter jsonDbImporter) {
        return args -> jsonDbImporter.importData();
    }
}
