package com.tienda.compraservice.infraestructure.adapters.exception;

public class ValidationException extends RuntimeException{
    public ValidationException(String message) {
        super(message);
    }
}
