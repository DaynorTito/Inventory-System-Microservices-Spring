package com.tienda.salieservice.infrastructure.adapters.exception;

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
