package com.tienda.com.tienda.inventoryserver.infraestructure.rest.advice;


import com.tienda.com.tienda.inventoryserver.domain.model.error.ErrorResponse;
import com.tienda.com.tienda.inventoryserver.infraestructure.adapters.exception.KardexNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class KardexControllerAdvice {

    /**
     * Handles KardexNotFoundException and returns a custom error response
     *
     * @param ex The exception thrown
     * @return A ResponseEntity with a custom error response for stock not found
     */
    @ExceptionHandler(KardexNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleKardexNotFoundException(KardexNotFoundException ex) {
        return new ResponseEntity<>(ErrorResponse.builder()
                .error(ex.getMessage())
                .userMessage("Kardex no encontrado, verifique los detalles e intente nuevamente")
                .status(HttpStatus.NOT_FOUND.name())
                .code(HttpStatus.NOT_FOUND.value())
                .build(), HttpStatus.NOT_FOUND);
    }
}
