package com.tienda.productoservice.infrastructure.rest.advice;

import com.tienda.productoservice.domain.model.error.ErrorResponse;
import com.tienda.productoservice.infrastructure.adapters.exception.CategoryNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CategoryControllerAdvice {

    /**
     * Handle CategoryNotFoundException
     *
     * @param ex the exception
     * @return the response entity with error details
     */
    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCategoryNotFoundException(CategoryNotFoundException ex) {
        return new ResponseEntity<>(ErrorResponse.builder()
                .error(ex.getMessage())
                .userMessage("Categoria no encontrada, verifique los detalles e intente nuevamente")
                .status(HttpStatus.NOT_FOUND.name())
                .code(HttpStatus.NOT_FOUND.value())
                .build(), HttpStatus.NOT_FOUND);
    }
}
