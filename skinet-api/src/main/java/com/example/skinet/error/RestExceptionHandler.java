package com.example.skinet.error;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;

import static java.util.stream.Collectors.*;

@RequiredArgsConstructor
@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private final Map<Class<? extends Exception>, HttpStatus> exceptionStatusMap = Map.of(
            AccessDeniedException.class, HttpStatus.FORBIDDEN,
            AuthenticationException.class, HttpStatus.UNAUTHORIZED
    );

    @ExceptionHandler(value = {Exception.class})
    ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(),
                mapExceptionToStatus(ex.getClass()), request);
    }

    @ExceptionHandler(value = {ApiException.class})
    ResponseEntity<Object> handleApiException(ApiException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(),
                ex.getStatusCode(), request);
    }

    //TODO handle some other exceptions (like TypeMismatch)
    @Override
    @NonNull
    protected ResponseEntity<Object> handleMethodArgumentNotValid(@NonNull MethodArgumentNotValidException ex, @NonNull HttpHeaders headers, @NonNull HttpStatus status, @NonNull WebRequest request) {
        Map<String, String> errors = ex.getBindingResult().getFieldErrors()
                .stream().collect(groupingBy(FieldError::getField,
                        mapping(FieldError::getDefaultMessage, joining("; "))));

        return handleExceptionInternal(ex, new ExtendedErrorResponse(status).withErrors(errors),
                headers, status, request);
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleExceptionInternal(@NonNull Exception ex, Object body, @NonNull HttpHeaders headers, @NonNull HttpStatus status, @NonNull WebRequest request) {
        ErrorResponse responseBody;
        if (body == null) {
            responseBody = new ErrorResponse(status);
        } else if (body instanceof ErrorResponse) {
            responseBody = (ErrorResponse) body;
        } else {
            responseBody = new ErrorResponse(status, body.toString());
        }

        headers.setContentType(MediaType.APPLICATION_JSON);
        return super.handleExceptionInternal(ex, responseBody,
                headers, status, request);
    }

    private HttpStatus mapExceptionToStatus(Class<? extends Exception> exceptionClass) {

        return exceptionStatusMap.entrySet().stream()
                .filter(e -> e.getKey().isAssignableFrom(exceptionClass))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
