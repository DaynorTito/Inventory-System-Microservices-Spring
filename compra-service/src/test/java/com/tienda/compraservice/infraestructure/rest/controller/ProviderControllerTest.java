package com.tienda.compraservice.infraestructure.rest.controller;

import com.tienda.compraservice.application.services.ProviderService;
import com.tienda.compraservice.domain.model.dto.request.ProviderRequest;
import com.tienda.compraservice.domain.model.dto.response.ProviderResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProviderControllerTest {

    @Mock
    private ProviderService providerService;

    @InjectMocks
    private ProviderController providerController;

    private UUID providerId;
    private ProviderRequest providerRequest;
    private ProviderResponse providerResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        providerId = UUID.randomUUID();

        providerRequest = ProviderRequest.builder()
                .name("Proveedor Test")
                .address("Calle 123")
                .phone("1234567890")
                .email("test@proveedor.com")
                .active(true)
                .build();

        providerResponse = ProviderResponse.builder()
                .id(providerId)
                .name("Proveedor Test")
                .address("Calle 123")
                .phone("1234567890")
                .email("test@proveedor.com")
                .active(true)
                .build();
    }


    @Test
    void getProviders_ShouldReturnListOfProviders() {
        when(providerService.getAllProviders()).thenReturn(List.of(providerResponse));

        ResponseEntity<List<ProviderResponse>> response = providerController.getProviders();

        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Proveedor Test", response.getBody().get(0).getName());
        verify(providerService, times(1)).getAllProviders();
    }

    @Test
    void getProviderById_ShouldReturnProvider() {
        when(providerService.getById(providerId)).thenReturn(providerResponse);

        ResponseEntity<ProviderResponse> response = providerController.getProviderById(providerId);

        assertNotNull(response.getBody());
        assertEquals(providerId, response.getBody().getId());
        verify(providerService, times(1)).getById(providerId);
    }

    @Test
    void createProvider_ShouldReturnCreatedProvider() {
        when(providerService.createEntity(providerRequest)).thenReturn(providerResponse);

        ResponseEntity<ProviderResponse> response = providerController.createProvider(providerRequest);

        assertNotNull(response.getBody());
        assertEquals(providerId, response.getBody().getId());
        assertEquals(201, response.getStatusCodeValue());
        verify(providerService, times(1)).createEntity(providerRequest);
    }

    @Test
    void updateProvider_ShouldReturnUpdatedProvider() {
        when(providerService.updateEntity(providerRequest, providerId)).thenReturn(providerResponse);

        ResponseEntity<ProviderResponse> response = providerController.updateProvider(providerId, providerRequest);

        assertNotNull(response.getBody());
        assertEquals(providerId, response.getBody().getId());
        verify(providerService, times(1)).updateEntity(providerRequest, providerId);
    }

    @Test
    void deleteProvider_ShouldReturnNoContent() {
        doNothing().when(providerService).deleteEntityById(providerId);

        ResponseEntity<Void> response = providerController.deleteProvider(providerId);

        assertEquals(204, response.getStatusCodeValue());
        verify(providerService, times(1)).deleteEntityById(providerId);
    }
}