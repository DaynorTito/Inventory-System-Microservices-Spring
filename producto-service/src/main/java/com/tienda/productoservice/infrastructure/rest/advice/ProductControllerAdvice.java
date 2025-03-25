package com.tienda.productoservice.infrastructure.rest.advice;

import com.tienda.productoservice.domain.model.error.ErrorResponse;
import com.tienda.productoservice.infrastructure.adapters.exception.ProductAlreadyExistsException;
import com.tienda.productoservice.infrastructure.adapters.exception.ProductNotFoundException;
import com.tienda.productoservice.infrastructure.adapters.exception.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ProductControllerAdvice {

    /**
     * Handle ProductNotFoundException when a product is not found in the database
     *
     * @param ex the exception
     * @return the response entity with error details specific to product not found
     */
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductNotFoundException(ProductNotFoundException ex) {
        return new ResponseEntity<>(ErrorResponse.builder()
                .error(ex.getMessage())
                .userMessage("Producto no encontrado, verifique los detalles e intente nuevamente")
                .status(HttpStatus.NOT_FOUND.name())
                .code(HttpStatus.NOT_FOUND.value())
                .build(), HttpStatus.NOT_FOUND);
    }

    /**
     * Handle ProductAlreadyExistsException when a product with the same details already exists in the database
     *
     * @param ex the exception
     * @return the response entity with error details specific to product already existing
     */
    @ExceptionHandler(ProductAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleProductAlreadyExistsException(ProductAlreadyExistsException ex) {
        return new ResponseEntity<>(ErrorResponse.builder()
                .error(ex.getMessage())
                .userMessage("Producto ya se encuentra registrado, verifique los detalles e intente nuevamente")
                .status(HttpStatus.CONFLICT.name())
                .code(HttpStatus.CONFLICT.value())
                .build(), HttpStatus.CONFLICT);
    }

    /**
     * Handle ValidationException when there are validation errors in the product data
     *
     * @param ex the exception
     * @return the response entity with error details specific to validation errors
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(ValidationException ex) {
        return new ResponseEntity<>(ErrorResponse.builder()
                .error(ex.getMessage())
                .userMessage("Error de validacion, verifique los detalles e intente nuevamente")
                .status(HttpStatus.BAD_REQUEST.name())
                .code(HttpStatus.BAD_REQUEST.value())
                .build(), HttpStatus.BAD_REQUEST);
    }
}
