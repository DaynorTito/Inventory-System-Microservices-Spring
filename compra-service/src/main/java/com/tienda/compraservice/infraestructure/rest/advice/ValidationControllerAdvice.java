package com.tienda.compraservice.infraestructure.rest.advice;


import com.tienda.compraservice.domain.model.error.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class ValidationControllerAdvice {

    /**
     * Handles MethodArgumentNotValidException
     *
     * @param ex The exception thrown when validation errors occur
     * @return A response entity containing error details
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
     * Handles MethodArgumentTypeMismatchException
     *
     * @param ex The exception thrown when argument type mismatch occurs
     * @return A response entity containing error details
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
