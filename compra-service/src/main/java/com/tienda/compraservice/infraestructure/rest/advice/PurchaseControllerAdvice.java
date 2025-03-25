package com.tienda.compraservice.infraestructure.rest.advice;

import com.tienda.compraservice.domain.model.error.ErrorResponse;
import com.tienda.compraservice.infraestructure.adapters.exception.PurchaseNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PurchaseControllerAdvice {

    /**
     * Handles PurchaseNotFoundException
     *
     * @param ex The exception thrown when a purchase is not found
     * @return A response entity containing error details
     */
    @ExceptionHandler(PurchaseNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePurchaseNotFoundException(PurchaseNotFoundException ex) {
        return new ResponseEntity<>(ErrorResponse.builder()
                .error(ex.getMessage())
                .userMessage("Compra no encontrada, verifique los detalles e intente nuevamente")
                .status(HttpStatus.NOT_FOUND.name())
                .code(HttpStatus.NOT_FOUND.value())
                .build(), HttpStatus.NOT_FOUND);
    }
}
