package com.tienda.compraservice.infraestructure.rest.advice;


import com.tienda.compraservice.domain.model.error.ErrorResponse;
import com.tienda.compraservice.infraestructure.adapters.exception.ProviderNotFoundException;
import com.tienda.compraservice.infraestructure.adapters.exception.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ProviderControllerAdvice {

    /**
     * Handles ProviderNotFoundException
     *
     * @param ex The exception thrown when a provider is not found
     * @return A response entity containing error details
     */
    @ExceptionHandler(ProviderNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProviderNotFoundException(ProviderNotFoundException ex) {
        return new ResponseEntity<>(ErrorResponse.builder()
                .error(ex.getMessage())
                .userMessage("Proveedor no encontrado, verifique los detalles e intente nuevamente")
                .status(HttpStatus.NOT_FOUND.name())
                .code(HttpStatus.NOT_FOUND.value())
                .build(), HttpStatus.NOT_FOUND);
    }

    /**
     * Handles ValidationException
     *
     * @param ex The exception thrown for validation errors
     * @return A response entity containing error details
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
