package com.theopus.restservice.controller;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { EntityNotFoundException.class})
    protected ResponseEntity<Object> handleEntityNotFound(RuntimeException ex, WebRequest request) {
        return ResponseEntity.status(404).body(ImmutablePair.of("error", ex.getMessage()));
    }

    @ExceptionHandler(value = { Exception.class})
    protected ResponseEntity<Object> handleOtherExceptions(RuntimeException ex, WebRequest request) {
        return ResponseEntity.status(500).body(ImmutablePair.of("error", ex.getMessage()));
    }
}
