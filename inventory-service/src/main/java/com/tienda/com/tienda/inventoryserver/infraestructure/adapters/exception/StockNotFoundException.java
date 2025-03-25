package com.tienda.com.tienda.inventoryserver.infraestructure.adapters.exception;

public class StockNotFoundException extends RuntimeException {
    public StockNotFoundException(String message) {
        super(message);
    }
}
