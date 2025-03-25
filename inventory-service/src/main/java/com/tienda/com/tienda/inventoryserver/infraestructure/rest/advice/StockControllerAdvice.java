package com.tienda.com.tienda.inventoryserver.infraestructure.rest.advice;


import com.tienda.com.tienda.inventoryserver.domain.model.error.ErrorResponse;
import com.tienda.com.tienda.inventoryserver.infraestructure.adapters.exception.InsufficientStock;
import com.tienda.com.tienda.inventoryserver.infraestructure.adapters.exception.StockNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class StockControllerAdvice {

    /**
     * Handles StockNotFoundException and returns a custom error response
     *
     * @param ex The exception thrown
     * @return A ResponseEntity with a custom error response for stock not found
     */
    @ExceptionHandler(StockNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleStockNotFoundException(StockNotFoundException ex) {
        return new ResponseEntity<>(ErrorResponse.builder()
                .error(ex.getMessage())
                .userMessage("Stock no encontrado, verifique los detalles e intente nuevamente")
                .status(HttpStatus.NOT_FOUND.name())
                .code(HttpStatus.NOT_FOUND.value())
                .build(), HttpStatus.NOT_FOUND);
    }

    /**
     * Handles InsufficientStock exception and returns a custom error response
     *
     * @param ex The exception thrown
     * @return A ResponseEntity with a custom error response for insufficient stock
     */
    @ExceptionHandler(InsufficientStock.class)
    public ResponseEntity<ErrorResponse> handleStockNotFoundException(InsufficientStock ex) {
        return new ResponseEntity<>(ErrorResponse.builder()
                .error(ex.getMessage())
                .userMessage("No existe suficiente stock para la operacion")
                .status(HttpStatus.NOT_FOUND.name())
                .code(HttpStatus.NOT_FOUND.value())
                .build(), HttpStatus.NOT_FOUND);
    }
}
