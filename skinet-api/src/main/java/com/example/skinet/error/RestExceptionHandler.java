package com.example.skinet.error;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.*;

@RequiredArgsConstructor
@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private final ErrorResponseBuilder responseBuilder;

    //TODO handle some other exceptions (like TypeMismatch)
    private final Map<Class<?>, Function<Object, Map<String, String>>> exceptionToErrors = Map.of(
            MethodArgumentNotValidException.class,
            ex -> convertValidationException((MethodArgumentNotValidException) ex)
    );

    @ExceptionHandler(value = {Exception.class})
    ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(),
                HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(value = {ApiException.class})
    ResponseEntity<Object> handleApiException(ApiException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(),
                ex.getStatusCode(), request);
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleExceptionInternal(@NonNull Exception ex, Object body, @NonNull HttpHeaders headers, @NonNull HttpStatus status, @NonNull WebRequest request) {
        Map<String, String> errors = exceptionToErrors
                .getOrDefault(ex.getClass(), o -> Collections.emptyMap())
                .apply(ex);
        if (body == null) {
            body = ApiException.getMessageForCode(status);
        }
        String responseBody = "";

        try {
            String message = body.toString();
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode node = responseBuilder.build(ex, status, message);
            if (!errors.isEmpty()) {
                ObjectNode errorsNode = mapper.createObjectNode();
                errors.forEach(errorsNode::put);
                node.set("errors", errorsNode);
            }
            responseBody = mapper.writer().writeValueAsString(node);
        } catch (Exception ignored) {
        }

        headers.setContentType(MediaType.APPLICATION_JSON);
        return super.handleExceptionInternal(ex, responseBody,
                headers, status, request);
    }

    private Map<String, String> convertValidationException(MethodArgumentNotValidException ex) {
        return ex.getBindingResult().getFieldErrors().stream()
                .collect(groupingBy(FieldError::getField,
                        mapping(FieldError::getDefaultMessage, joining("; "))));
    }
}
