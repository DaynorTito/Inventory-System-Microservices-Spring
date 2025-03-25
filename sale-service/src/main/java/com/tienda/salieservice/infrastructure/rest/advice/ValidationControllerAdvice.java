package com.tienda.salieservice.infrastructure.rest.advice;


import com.tienda.salieservice.domain.model.error.ErrorResponse;
import com.tienda.salieservice.infrastructure.adapters.exception.ValidationException;
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
     * Handles ValidationException errors
     *
     * @param ex the ValidationException to be handled
     * @return a ResponseEntity containing the error details and a user-friendly message
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

    /**
     * Handles MethodArgumentTypeMismatchException errors
     *
     * This occurs when a method argument's type doesn't match the expected type
     * @param ex the MethodArgumentTypeMismatchException to be handled
     * @return a ResponseEntity containing the error details and a user-friendly message
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

    /**
     * Handles MethodArgumentNotValidException errors
     *
     * This occurs when method arguments fail validation (e.g., invalid input)
     * @param ex the MethodArgumentNotValidException to be handled
     * @return a ResponseEntity containing the validation errors and a user-friendly message
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
