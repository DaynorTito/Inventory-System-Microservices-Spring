package com.tienda.com.tienda.inventoryserver.infraestructure.adapters.exception;

public class ValidationException extends RuntimeException{
    public ValidationException(String message) {
        super(message);
    }
}
