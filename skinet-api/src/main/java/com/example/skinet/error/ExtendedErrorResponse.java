package com.example.skinet.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public class ExtendedErrorResponse extends ErrorResponse {

    @Getter
    private final Map<String, String> errors = new HashMap<>();

    public ExtendedErrorResponse(HttpStatus statusCode) {
        super(statusCode);
    }

    public ExtendedErrorResponse withErrors(Map<String, String> newErrors) {
        errors.putAll(newErrors);
        return this;
    }
}
