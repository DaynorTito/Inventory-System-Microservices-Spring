package com.tienda.compraservice.application.validator;

import com.tienda.compraservice.domain.model.dto.request.ProductRequest;
import com.tienda.compraservice.domain.model.dto.request.PurchaseInventoryRequest;
import com.tienda.compraservice.domain.model.dto.response.ProductResponse;
import com.tienda.compraservice.infraestructure.adapters.client.InventoryFeignClient;
import com.tienda.compraservice.infraestructure.adapters.client.ProductFeignClient;
import com.tienda.compraservice.infraestructure.adapters.exception.ValidationException;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)

class FeignValidatorTest {
    @Mock
    private InventoryFeignClient inventoryFeignClient;

    @Mock
    private ProductFeignClient productFeignClient;

    @InjectMocks
    private FeignValidator feignValidator;

    @BeforeEach
    void setUp() {
        feignValidator = new FeignValidator(inventoryFeignClient, productFeignClient);
    }

    @Test
    void registerPurchase_ShouldCallInventoryFeignClient() {
        PurchaseInventoryRequest request = new PurchaseInventoryRequest(
                10, BigDecimal.TEN, UUID.randomUUID(), "prod123", null);
        assertDoesNotThrow(() -> feignValidator.registerPurchase(request));
    }

    @Test
    void registerPurchase_ShouldThrowValidationExceptionWhenFeignExceptionOccurs() {
        PurchaseInventoryRequest request = new PurchaseInventoryRequest(
                10, BigDecimal.TEN, UUID.randomUUID(), "prod123", null);
        doThrow(FeignException.class).when(inventoryFeignClient).registerInputInventory(any());
        assertThrows(ValidationException.class, () -> feignValidator.registerPurchase(request));
    }

    @Test
    void createProductIntoService_ShouldReturnProductResponse() {
        ProductRequest productRequest = new ProductRequest(
                "Laptop", "High-end laptop", BigDecimal.valueOf(1200), "BrandX", "Electronics");
        ProductResponse expectedResponse = new ProductResponse(
                "prod123", "Laptop", "High-end laptop", null, null, BigDecimal.valueOf(1200), null);
        when(productFeignClient.createProduct(any())).thenReturn(expectedResponse);

        ProductResponse actualResponse = feignValidator.createProductIntoService(productRequest);

        assertNotNull(actualResponse);
        assertEquals(expectedResponse.getCod(), actualResponse.getCod());
    }

    @Test
    void createProductIntoService_ShouldThrowValidationExceptionWhenFeignExceptionOccurs() {
        ProductRequest productRequest = new ProductRequest(
                "Laptop", "High-end laptop", BigDecimal.valueOf(1200), "BrandX", "Electronics");
        doThrow(FeignException.class).when(productFeignClient).createProduct(any());

        assertThrows(ValidationException.class, () -> feignValidator.createProductIntoService(productRequest));
    }

    @Test
    void verifyProductIntoService_ShouldNotThrowExceptionWhenProductExists() {
        assertDoesNotThrow(() -> feignValidator.verifyProductIntoService("prod123"));
    }

    @Test
    void verifyProductIntoService_ShouldThrowValidationExceptionWhenFeignExceptionOccurs() {
        doThrow(FeignException.class).when(productFeignClient).getProductByCod(anyString());

        assertThrows(ValidationException.class, () -> feignValidator.verifyProductIntoService("prod123"));
    }
}