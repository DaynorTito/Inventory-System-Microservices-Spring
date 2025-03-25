package com.tienda.com.tienda.inventoryserver.application.validation;


import com.tienda.com.tienda.inventoryserver.domain.model.dto.Product;
import com.tienda.com.tienda.inventoryserver.infraestructure.adapters.client.ProductFeignClient;
import com.tienda.com.tienda.inventoryserver.infraestructure.adapters.client.PurchaseFeignClient;
import com.tienda.com.tienda.inventoryserver.infraestructure.adapters.exception.ValidationException;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class FeignValidatorTest {
    @Mock
    private ProductFeignClient productFeignClient;

    @Mock
    private PurchaseFeignClient purchaseFeignClient;

    @InjectMocks
    private FeignValidator feignValidator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void verifyExistingProduct_ShouldThrowValidationException_WhenFeignExceptionOccurs() {
        String productCode = "prod-001";
        FeignException feignException = mock(FeignException.class);
        when(productFeignClient.getProductByCod(productCode)).thenThrow(feignException);
        ValidationException thrown = assertThrows(ValidationException.class, () -> feignValidator.verifyExistingProduct(productCode));
        assertEquals("Error en informacion del producto: Error desconocido", thrown.getMessage());
    }

    @Test
    public void verifyExistingProduct_ShouldNotThrowException_WhenProductExists() {
        String productCode = "prod-001";
        when(productFeignClient.getProductByCod(productCode)).thenReturn(new Product());
        assertDoesNotThrow(() -> feignValidator.verifyExistingProduct(productCode));
    }

    @Test
    public void verifyExistingProvider_ShouldThrowValidationException_WhenIllegalArgumentExceptionOccurs() {
        UUID invalidProviderId = UUID.randomUUID();
        when(purchaseFeignClient.getProviderById(invalidProviderId)).thenThrow(IllegalArgumentException.class);
        ValidationException thrown = assertThrows(ValidationException.class,
                () -> feignValidator.verifyExistingProvider(invalidProviderId));
        assertEquals("El ID del proveedor no es un UUID valido", thrown.getMessage());
    }

    @Test
    public void verifyExistingProvider_ShouldThrowValidationException_WhenFeignExceptionOccurs() {
        UUID providerId = UUID.randomUUID();
        FeignException feignException = mock(FeignException.class);
        when(purchaseFeignClient.getProviderById(providerId)).thenThrow(feignException);
        ValidationException thrown = assertThrows(ValidationException.class,
                () -> feignValidator.verifyExistingProvider(providerId));
        assertEquals("Error en informacion del proveedor: Error desconocido", thrown.getMessage());
    }

    @Test
    public void verifyExistingProvider_ShouldNotThrowException_WhenProviderExists() {
        UUID providerId = UUID.randomUUID();
        when(purchaseFeignClient.getProviderById(providerId)).thenReturn("provider-details");
        assertDoesNotThrow(() -> feignValidator.verifyExistingProvider(providerId));
    }
}
