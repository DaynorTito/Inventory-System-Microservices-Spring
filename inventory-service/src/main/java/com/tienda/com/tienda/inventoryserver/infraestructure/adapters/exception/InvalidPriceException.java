package com.tienda.com.tienda.inventoryserver.infraestructure.adapters.exception;

public class InvalidPriceException extends RuntimeException {
    public InvalidPriceException(String message) {
        super(message);
    }
}
