package com.example.coffeeshop.controller;

import com.google.genai.errors.ClientException;
import com.example.coffeeshop.dto.ErrorResponse;
import com.example.coffeeshop.exception.InvalidOrderException;
import com.example.coffeeshop.exception.InvalidStatusTransitionException;
import com.example.coffeeshop.exception.MenuItemNotFoundException;
import com.example.coffeeshop.exception.OrderNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({
            MenuItemNotFoundException.class, OrderNotFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleNotFound(RuntimeException ex, HttpServletRequest request) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(InvalidStatusTransitionException.class)
    public ResponseEntity<ErrorResponse> handleConflict(RuntimeException ex, HttpServletRequest request) {
        return build(HttpStatus.CONFLICT, ex.getMessage(), request);
    }

    @ExceptionHandler(InvalidOrderException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(RuntimeException ex, HttpServletRequest request) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        return build(
                HttpStatus.BAD_REQUEST,
                ex.getBindingResult()
                  .getFieldErrors()
                  .stream()
                  .map(this::formatFieldError)
                  .collect(Collectors.joining("; ")),
                request
        );
    }

    @ExceptionHandler(ClientException.class)
    public ResponseEntity<ErrorResponse> handleGenAIClientException(ClientException ex, HttpServletRequest request) {
        return build(
            HttpStatus.BAD_GATEWAY,
            "The AI service is temporarily unavailable due to invalid authentication credentials.",
            request
        );
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleGenericRuntime(RuntimeException ex, HttpServletRequest request) {
        return build(
            HttpStatus.INTERNAL_SERVER_ERROR,
            ex.getMessage(),
            request
        );
    }

    private String formatFieldError(FieldError fieldError) {
        if (fieldError == null)
            return "Unknown: unknown error";

        return "%s: %s".formatted(fieldError.getField(), fieldError.getDefaultMessage());
    }

    private ResponseEntity<ErrorResponse> build(HttpStatus status, String message, HttpServletRequest request) {
        var body = new ErrorResponse(status.value(), status.getReasonPhrase(), message, request.getRequestURI());

        return ResponseEntity.status(status)
                             .body(body);
    }
}
