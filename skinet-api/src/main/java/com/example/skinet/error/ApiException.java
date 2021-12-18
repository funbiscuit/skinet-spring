package com.example.skinet.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiException extends RuntimeException {
    private final HttpStatus statusCode;
    private final String message;

    public ApiException(HttpStatus statusCode) {
        this(statusCode, null);
    }

    public ApiException(HttpStatus statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}
