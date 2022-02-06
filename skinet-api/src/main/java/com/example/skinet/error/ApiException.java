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

    public static ApiException emailNotFound(String email) {
        return new ApiException(HttpStatus.BAD_REQUEST,
                String.format("Email %s not found", email));
    }

    public static ApiException addressNotFound(String email) {
        return new ApiException(HttpStatus.NOT_FOUND,
                String.format("User %s does not have an address", email));
    }

    public static ApiException basketNotFound(String basketId) {
        return new ApiException(HttpStatus.NOT_FOUND,
                String.format("Basket %s was not found", basketId));
    }
}
