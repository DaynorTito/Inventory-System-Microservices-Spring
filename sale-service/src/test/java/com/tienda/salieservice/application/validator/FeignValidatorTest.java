package com.tienda.salieservice.application.validator;


import com.tienda.salieservice.domain.model.dto.Product;
import com.tienda.salieservice.domain.model.dto.request.SaleInventoryRequest;
import com.tienda.salieservice.infrastructure.adapters.client.InventoryFeignClient;
import com.tienda.salieservice.infrastructure.adapters.client.ProductFeignClient;
import com.tienda.salieservice.infrastructure.adapters.exception.ValidationException;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeignValidatorTest {

    @Mock
    private ProductFeignClient productFeignClient;

    @Mock
    private InventoryFeignClient inventoryFeignClient;

    @InjectMocks
    private FeignValidator feignValidator;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setCod("P123");
        product.setName("Producto Test");
        product.setSalePrice(new BigDecimal("100.00"));
    }

    @Test
    void verifyExistingProduct_Success() {
        when(productFeignClient.getProductByCod("P123")).thenReturn(product);
        Product result = feignValidator.verifyExistingProduct("P123");
        assertNotNull(result);
        assertEquals("P123", result.getCod());
    }

    @Test
    void verifyExistingProduct_ThrowsValidationException() {
        when(productFeignClient.getProductByCod("P123"))
                .thenThrow(mock(FeignException.class));

        ValidationException exception = assertThrows(ValidationException.class,
                () -> feignValidator.verifyExistingProduct("P123"));

        assertTrue(exception.getMessage().contains("Error con el producto"));
    }

    @Test
    void registerSale_Success() {
        SaleInventoryRequest request = new SaleInventoryRequest(5, "P123", new BigDecimal("100.00"));
        doNothing().when(inventoryFeignClient).registerOutputInventory(request);
        assertDoesNotThrow(() -> feignValidator.registerSale(request));
    }

    @Test
    void registerSale_ThrowsValidationException() {
        SaleInventoryRequest request = new SaleInventoryRequest(5, "P123", new BigDecimal("100.00"));
        doThrow(mock(FeignException.class)).when(inventoryFeignClient).registerOutputInventory(request);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> feignValidator.registerSale(request));

        assertTrue(exception.getMessage().contains("Error en informacion del producto"));
    }
}