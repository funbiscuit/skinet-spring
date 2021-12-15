package com.example.skinet.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@ConfigurationProperties("app")
@Getter
@Setter
@Validated
public class AppConfigProperties {
    @NotBlank
    private String rootUrl;
}
