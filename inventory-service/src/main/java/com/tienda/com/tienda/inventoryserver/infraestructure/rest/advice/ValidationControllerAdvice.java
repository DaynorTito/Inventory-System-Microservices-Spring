package com.tienda.com.tienda.inventoryserver.infraestructure.rest.advice;

import com.tienda.com.tienda.inventoryserver.domain.model.error.ErrorResponse;
import com.tienda.com.tienda.inventoryserver.infraestructure.adapters.exception.InvalidPriceException;
import com.tienda.com.tienda.inventoryserver.infraestructure.adapters.exception.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class ValidationControllerAdvice {

    /**
     * Handles InvalidPriceException and returns a custom error response
     *
     * @param ex The exception thrown
     * @return A ResponseEntity with a custom error response for invalid price
     */
    @ExceptionHandler(InvalidPriceException.class)
    public ResponseEntity<ErrorResponse> handleInvalidPriceNotFoundException(InvalidPriceException ex) {
        return new ResponseEntity<>(ErrorResponse.builder()
                .error(ex.getMessage())
                .userMessage("Precio invalido, verifique los detalles e intente nuevamente")
                .status(HttpStatus.BAD_REQUEST.name())
                .code(HttpStatus.BAD_REQUEST.value())
                .build(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles ValidationException and returns a custom error response
     *
     * @param ex The exception thrown
     * @return A ResponseEntity with a custom error response for validation error
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleInvalidPriceNotFoundException(ValidationException ex) {
        return new ResponseEntity<>(ErrorResponse.builder()
                .error(ex.getMessage())
                .userMessage("Error de validacion, verifique los detalles e intente nuevamente")
                .status(HttpStatus.BAD_REQUEST.name())
                .code(HttpStatus.BAD_REQUEST.value())
                .build(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles MethodArgumentNotValidException and returns a custom error response
     *
     * @param ex The exception thrown
     * @return A ResponseEntity with a custom error response for validation errors
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        StringBuilder errorMessages = new StringBuilder();
        bindingResult.getFieldErrors().forEach(fieldError -> {
            errorMessages.append(fieldError.getDefaultMessage()).append(" ");
        });
        return new ResponseEntity<>(ErrorResponse.builder()
                .error("Error de validación")
                .userMessage(errorMessages.toString())
                .status(HttpStatus.BAD_REQUEST.name())
                .code(HttpStatus.BAD_REQUEST.value())
                .build(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles HttpMessageNotReadableException and returns a custom error response
     *
     * @param ex The exception thrown
     * @return A ResponseEntity with a custom error response for validation errors
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleInvalidDateFormat(HttpMessageNotReadableException ex) {
        return new ResponseEntity<>(ErrorResponse.builder()
                .error("Formato datos invalido")
                .userMessage("Verifique los formatos de fehcas y tipos de datos")
                .status(HttpStatus.BAD_REQUEST.name())
                .code(HttpStatus.BAD_REQUEST.value())
                .build(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles MethodArgumentTypeMismatchException and returns a custom error response
     *
     * @param ex The exception thrown
     * @return A ResponseEntity with a custom error response for validation errors
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return new ResponseEntity<>(ErrorResponse.builder()
                .error(ex.getMessage())
                .userMessage("Formato invalido")
                .status(HttpStatus.BAD_REQUEST.name())
                .code(HttpStatus.BAD_REQUEST.value())
                .build(), HttpStatus.BAD_REQUEST);
    }
}
