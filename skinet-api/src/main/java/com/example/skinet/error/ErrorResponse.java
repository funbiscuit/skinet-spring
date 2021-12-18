package com.example.skinet.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class ErrorResponse {
    private final HttpStatus statusCode;
    @Getter
    private final String message;

    public ErrorResponse(HttpStatus statusCode) {
        this(statusCode, getMessageForCode(statusCode));
    }

    public ErrorResponse(HttpStatus statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public static String getMessageForCode(HttpStatus statusCode) {
        return switch (statusCode) {
            case BAD_REQUEST -> "A bad request, you have made";
            case UNAUTHORIZED -> "Authorized, you are not";
            case NOT_FOUND -> "Resource found... NOT!";
            case INTERNAL_SERVER_ERROR -> "Errors are the path to the dark side. Errors lead to anger. Anger leads to hate. Hate leads to career change";
            default -> "";
        };
    }

    public int getStatus() {
        return statusCode.value();
    }
}
