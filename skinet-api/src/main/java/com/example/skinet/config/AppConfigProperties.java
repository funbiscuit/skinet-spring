package com.example.skinet.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ConfigurationProperties("app")
@Getter
@Setter
@Validated
public class AppConfigProperties {
    @NotBlank
    private String rootUrl;

    @NotNull
    private JwtConfigProperties jwt;

    @NotNull
    private StripeProperties stripe;

    @Getter
    @Setter
    public static class JwtConfigProperties {
        @NotBlank
        private String secret;

        @NotBlank
        private String issuer;

        private int daysToExpire;
    }

    @Getter
    @Setter
    public static class StripeProperties {
        @NotBlank
        private String publishableKey;

        @NotBlank
        private String secretKey;
    }
}
