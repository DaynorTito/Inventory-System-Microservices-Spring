package com.tienda.productoservice.infrastructure.rest.controller;

import com.tienda.productoservice.application.services.ProductService;
import com.tienda.productoservice.domain.model.dto.request.ProductRequest;
import com.tienda.productoservice.domain.model.dto.response.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {
    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private ProductRequest productRequest;
    private ProductResponse productResponse;

    @BeforeEach
    void setUp() {
        productRequest = new ProductRequest("Laptop", "Gaming Laptop", BigDecimal.valueOf(500),
                BigDecimal.valueOf(1000),"Electronics", "Dell");
        productResponse = new ProductResponse("P001", "Laptop", "Gaming Laptop", null,
                null, BigDecimal.valueOf(500), BigDecimal.valueOf(1000), LocalDateTime.now());
    }

    @Test
    void getProducts_ShouldReturnProducts() {
        List<ProductResponse> productResponses = List.of(productResponse);
        when(productService.getAllProducts()).thenReturn(productResponses);

        ResponseEntity<List<ProductResponse>> response = productController.getProducts();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getProductByCod_ShouldReturnProduct() {
        when(productService.getById("P001")).thenReturn(productResponse);

        ResponseEntity<ProductResponse> response = productController.getProductByCod("P001");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Laptop", Objects.requireNonNull(response.getBody()).getName());
    }

    @Test
    void createProduct_ShouldCreateProduct() {
        when(productService.createEntity(productRequest)).thenReturn(productResponse);

        ResponseEntity<ProductResponse> response = productController.createProduct(productRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Laptop", Objects.requireNonNull(response.getBody()).getName());
    }

    @Test
    void updateProduct_ShouldUpdateProduct() {
        when(productService.updateEntity(productRequest, "P001")).thenReturn(productResponse);

        ResponseEntity<ProductResponse> response = productController.updateProduct("P001", productRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Laptop", Objects.requireNonNull(response.getBody()).getName());
    }

    @Test
    void deleteProduct_ShouldDeleteProduct() {
        doNothing().when(productService).deleteEntityById("P001");

        ResponseEntity<Void> response = productController.deleteProduct("P001");

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(productService, times(1)).deleteEntityById("P001");
    }

    @Test
    void getProductsByCategoryBrandAndPrice_ShouldReturnFilteredProducts() {
        List<ProductResponse> productResponses = List.of(productResponse);
        when(productService.findAllByCategoryBrandAndPrice("Electronics", "Dell",
                BigDecimal.valueOf(500), BigDecimal.valueOf(1000)))
                .thenReturn(productResponses);

        ResponseEntity<List<ProductResponse>> response = productController
                .getProductsByCategoryBrandAndPrice("Electronics", "Dell",
                        BigDecimal.valueOf(500), BigDecimal.valueOf(1000));

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, Objects.requireNonNull(response.getBody()).size());
    }

    @Test
    void getProductsByWord_ShouldReturnProductsMatchingWord() {
        List<ProductResponse> productResponses = List.of(productResponse);
        when(productService.findAllByWord("Laptop")).thenReturn(productResponses);

        ResponseEntity<List<ProductResponse>> response = productController.getProductsByWord("Laptop");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, Objects.requireNonNull(response.getBody()).size());
    }
}