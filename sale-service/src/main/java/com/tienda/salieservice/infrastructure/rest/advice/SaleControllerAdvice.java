package com.tienda.salieservice.infrastructure.rest.advice;

import com.tienda.salieservice.domain.model.error.ErrorResponse;
import com.tienda.salieservice.infrastructure.adapters.exception.SaleDetailNotFound;
import com.tienda.salieservice.infrastructure.adapters.exception.SaleNotFOund;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SaleControllerAdvice {

    /**
     * Handles SaleDetailNotFound errors
     *
     * @param ex the SaleDetailNotFound exception to be handled
     * @return a ResponseEntity containing the error details and a user-friendly message
     */
    @ExceptionHandler(SaleDetailNotFound.class)
    public ResponseEntity<ErrorResponse> handleDetailsNotFoundException(SaleDetailNotFound ex) {
        return new ResponseEntity<>(ErrorResponse.builder()
                .error(ex.getMessage())
                .userMessage("Detalle no encontrado, verifique los detalles e intente nuevamente")
                .status(HttpStatus.NOT_FOUND.name())
                .code(HttpStatus.NOT_FOUND.value())
                .build(), HttpStatus.NOT_FOUND);
    }

    /**
     * Handles SaleNotFOund errors
     *
     * @param ex the SaleNotFOund exception to be handled
     * @return a ResponseEntity containing the error details and a user-friendly message
     */
    @ExceptionHandler(SaleNotFOund.class)
    public ResponseEntity<ErrorResponse> handleSaleNotFoundException(SaleNotFOund ex) {
        return new ResponseEntity<>(ErrorResponse.builder()
                .error(ex.getMessage())
                .userMessage("Error de validacion, verifique los detalles e intente nuevamente")
                .status(HttpStatus.NOT_FOUND.name())
                .code(HttpStatus.NOT_FOUND.value())
                .build(), HttpStatus.NOT_FOUND);
    }
}
