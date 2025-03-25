package com.tienda.compraservice.infraestructure.adapters.exception;

public class PurchaseNotFoundException extends RuntimeException{
    public PurchaseNotFoundException(String message) {
        super(message);
    }
}
