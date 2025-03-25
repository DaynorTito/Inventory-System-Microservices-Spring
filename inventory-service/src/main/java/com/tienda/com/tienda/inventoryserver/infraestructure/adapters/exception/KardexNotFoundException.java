package com.tienda.com.tienda.inventoryserver.infraestructure.adapters.exception;

public class KardexNotFoundException extends RuntimeException {
    public KardexNotFoundException(String message) {
        super(message);
    }
}
