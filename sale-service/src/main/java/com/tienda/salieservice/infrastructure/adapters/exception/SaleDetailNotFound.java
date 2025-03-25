package com.tienda.salieservice.infrastructure.adapters.exception;

public class SaleDetailNotFound extends RuntimeException {
    public SaleDetailNotFound(String message) {
        super(message);
    }
}
