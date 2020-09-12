package com.keigo.WebQuizEngine.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class ControllerExceptionHandler {

    private static final String NOT_FOUND_MESSAGE = "Id not found for quiz";

    @ExceptionHandler(NoSuchElementException.class)
    protected ResponseEntity<HashMap<String, String>> handleIndexOutOfBoundsException(NoSuchElementException e) {
        HashMap<String, String> response = new HashMap<>();
        response.put("message", NOT_FOUND_MESSAGE);
        response.put("error", e.getClass().getSimpleName());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    // TODO could not resolve parameter (parameter != @exceptionhandler)
    @ExceptionHandler({MethodArgumentNotValidException.class, ResponseStatusException.class})
    protected ResponseEntity<HashMap<String, String>> handleValidationException(MethodArgumentNotValidException e) {
        HashMap<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errors);
    }
}
