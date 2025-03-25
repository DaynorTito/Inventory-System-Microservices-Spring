package com.tienda.productoservice.infrastructure.adapters.exception;

public class BrandNotFoundException extends RuntimeException{
    public BrandNotFoundException(String message) {
        super(message);
    }
}
