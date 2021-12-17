package com.example.skinet.error;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@Profile("!dev")
public class DefaultErrorResponseBuilder implements ErrorResponseBuilder {
    @Override
    public ObjectNode build(Exception cause, HttpStatus code, String message) {
        ObjectMapper mapper = new ObjectMapper();

        ObjectNode node = mapper.createObjectNode();
        node.put("status", code.value());
        node.put("message", message);

        return node;
    }
}
