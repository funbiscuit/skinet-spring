package com.example.skinet;

import com.example.skinet.config.AppConfigProperties;
import com.example.skinet.core.entity.Product;
import com.example.skinet.core.entity.ProductDTO;
import com.example.skinet.core.entity.order.Order;
import com.example.skinet.core.entity.order.OrderItem;
import com.example.skinet.core.entity.order.OrderStatus;
import com.example.skinet.dto.OrderDto;
import com.example.skinet.dto.OrderItemDto;
import com.example.skinet.service.JsonDbImporter;
import com.example.skinet.service.UsersInitializer;
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
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;

import java.util.Locale;

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
        Converter<OrderStatus, String> orderStatusToString = ctx -> ctx.getSource().getValue();

        TypeMap<Product, ProductDTO> typeMap = mapper.createTypeMap(Product.class, ProductDTO.class);
        // this could be done by just overriding toSting, but in case we need
        // different conversions in different places, do it this way
        typeMap.addMapping(p -> p.getBrand().getName(), ProductDTO::setBrand);
        typeMap.addMapping(p -> p.getType().getName(), ProductDTO::setType);

        typeMap.addMappings(m -> m.using(urlToGlobal)
                .map(Product::getPictureUrl, ProductDTO::setPictureUrl));

        TypeMap<Order, OrderDto> orderToDtoMap = mapper.createTypeMap(Order.class, OrderDto.class);
        orderToDtoMap.addMapping(src -> src.getDeliveryMethod().getPrice(), OrderDto::setShippingPrice);
        orderToDtoMap.addMapping(src -> src.getDeliveryMethod().getShortName(), OrderDto::setDeliveryMethod);
        orderToDtoMap.addMappings(m -> m.using(orderStatusToString)
                .map(Order::getStatus, OrderDto::setStatus));

        TypeMap<OrderItem, OrderItemDto> orderItemToDtoMap = mapper.createTypeMap(OrderItem.class, OrderItemDto.class);
        orderItemToDtoMap.addMapping(src -> src.getItemOrdered().getProductId(), OrderItemDto::setProductId);
        orderItemToDtoMap.addMapping(src -> src.getItemOrdered().getProductName(), OrderItemDto::setProductName);
        orderItemToDtoMap.addMappings(m -> m.using(urlToGlobal)
                .map(src -> src.getItemOrdered().getPictureUrl(), OrderItemDto::setPictureUrl));

        return mapper;
    }

    @Bean
    public LocaleResolver localeResolver() {
        return new FixedLocaleResolver(Locale.ENGLISH);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public CommandLineRunner dbInitializer(JsonDbImporter jsonDbImporter,
                                           UsersInitializer usersInitializer) {
        return args -> {
            jsonDbImporter.importData();
            usersInitializer.seedUsers();
        };
    }
}
