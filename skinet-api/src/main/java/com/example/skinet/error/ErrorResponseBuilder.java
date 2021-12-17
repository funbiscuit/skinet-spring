package com.example.skinet.error;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.HttpStatus;

public interface ErrorResponseBuilder {
    ObjectNode build(Exception cause, HttpStatus code, String message);
}
