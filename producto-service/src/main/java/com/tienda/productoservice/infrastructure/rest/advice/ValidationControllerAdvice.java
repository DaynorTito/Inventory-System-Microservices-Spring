package com.tienda.productoservice.infrastructure.rest.advice;


import com.tienda.productoservice.domain.model.error.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ValidationControllerAdvice {

    /**
     * Handle MethodArgumentNotValidException when validation errors occur on request parameters
     *
     * @param ex the exception
     * @return the response entity with error details specific to validation errors
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
}
