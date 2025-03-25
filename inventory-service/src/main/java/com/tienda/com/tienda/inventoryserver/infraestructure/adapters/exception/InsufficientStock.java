package com.tienda.com.tienda.inventoryserver.infraestructure.adapters.exception;

public class InsufficientStock extends RuntimeException {
    public InsufficientStock(String message) {
        super(message);
    }
}
