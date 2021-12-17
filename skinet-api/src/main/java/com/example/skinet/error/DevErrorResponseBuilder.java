package com.example.skinet.error;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.io.StringWriter;

@Component
@Profile("dev")
public class DevErrorResponseBuilder extends DefaultErrorResponseBuilder {
    @Override
    public ObjectNode build(Exception cause, HttpStatus code, String message) {
        ObjectNode node = super.build(cause, code, message);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        cause.printStackTrace(writer);

        node.put("details", stringWriter.toString());

        return node;
    }
}
