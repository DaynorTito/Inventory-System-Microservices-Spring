package com.tienda.productoservice.application.services;


import com.tienda.productoservice.application.mapper.ProductDomainMapper;
import com.tienda.productoservice.domain.model.dto.Brand;
import com.tienda.productoservice.domain.model.dto.Category;
import com.tienda.productoservice.domain.model.dto.Product;
import com.tienda.productoservice.domain.model.dto.request.ProductRequest;
import com.tienda.productoservice.domain.model.dto.response.ProductResponse;
import com.tienda.productoservice.domain.port.BrandPersistancePort;
import com.tienda.productoservice.domain.port.CategoryPersistancePort;
import com.tienda.productoservice.domain.port.ProductPersistancePort;
import com.tienda.productoservice.infrastructure.adapters.exception.ProductNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductPersistancePort productPersistancePort;

    @Mock
    private CategoryPersistancePort categoryPersistancePort;

    @Mock
    private BrandPersistancePort brandPersistancePort;

    @Mock
    private ProductDomainMapper productMapper;

    @InjectMocks
    private ProductService productService;

    private Product product;
    private ProductRequest productRequest;
    private ProductResponse productResponse;
    private Category category;
    private Brand brand;

    @BeforeEach
    void setUp() {
        category = new Category(1L, "Electronics", "Electronics category description");
        brand = new Brand(1L, "Samsung", "Samsung brand description");

        productRequest = ProductRequest.builder()
                .name("Galaxy S21")
                .description("Smartphone")
                .category("Electronics")
                .brand("Samsung")
                .salePrice(new BigDecimal("799.99"))
                .discount(new BigDecimal("50.00"))
                .build();

        product = Product.builder()
                .cod("PRD1234")
                .name("Galaxy S21")
                .description("Smartphone")
                .category(category)
                .brand(brand)
                .salePrice(new BigDecimal("799.99"))
                .creationDate(LocalDateTime.now())
                .discount(new BigDecimal("50.00"))
                .build();
        productResponse = ProductResponse.builder()
                .cod("PRD1234")
                .name("Galaxy S21")
                .description("Smartphone")
                .category(category)
                .brand(brand)
                .salePrice(new BigDecimal("799.99"))
                .discount(new BigDecimal("50.00"))
                .creationDate(LocalDateTime.now())
                .build();
    }

    @Test
    void getById_ShouldReturnProductResponse_WhenProductExists() {
        when(productPersistancePort.readById("PRD1234")).thenReturn(product);
        when(productMapper.toResponse(product)).thenReturn(productResponse);

        ProductResponse result = productService.getById("PRD1234");

        assertNotNull(result);
        assertEquals("PRD1234", result.getCod());
        assertEquals("Galaxy S21", result.getName());
        verify(productPersistancePort, times(1)).readById("PRD1234");
    }

    @Test
    void getById_ShouldThrowException_WhenProductDoesNotExist() {
        when(productPersistancePort.readById("PRD9999")).thenReturn(null);

        assertThrows(ProductNotFoundException.class, () -> productService.getById("PRD9999"));
    }

    @Test
    void createEntity_ShouldReturnProductResponse_WhenValidRequest() {
        when(productMapper.toDomainFromReq(productRequest)).thenReturn(product);
        when(categoryPersistancePort.findCategoryByName("electronics")).thenReturn(category);
        when(brandPersistancePort.findBrandByName("samsung")).thenReturn(brand);
        when(productPersistancePort.create(any(Product.class))).thenReturn(product);
        when(productMapper.toResponse(product)).thenReturn(productResponse);

        ProductResponse result = productService.createEntity(productRequest);

        assertNotNull(result);
        assertEquals("Galaxy S21", result.getName());
        verify(productPersistancePort, times(1)).create(any(Product.class));
    }

    @Test
    void deleteEntityById_ShouldDeleteProduct_WhenProductExists() {
        when(productPersistancePort.readById("PRD1234")).thenReturn(product);
        doNothing().when(productPersistancePort).deleteById("PRD1234");

        assertDoesNotThrow(() -> productService.deleteEntityById("PRD1234"));
        verify(productPersistancePort, times(1)).deleteById("PRD1234");
    }

    @Test
    void deleteEntityById_ShouldThrowException_WhenProductDoesNotExist() {
        when(productPersistancePort.readById("PRD9999")).thenReturn(null);

        assertThrows(ProductNotFoundException.class, () -> productService.deleteEntityById("PRD9999"));
    }
}