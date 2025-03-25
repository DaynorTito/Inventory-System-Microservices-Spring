package com.tienda.compraservice.application.services;

import com.tienda.compraservice.application.mapper.ProviderDomainMapper;
import com.tienda.compraservice.domain.model.dto.Provider;
import com.tienda.compraservice.domain.model.dto.request.ProviderRequest;
import com.tienda.compraservice.domain.model.dto.response.ProviderResponse;
import com.tienda.compraservice.domain.port.ProviderPersistancePort;
import com.tienda.compraservice.infraestructure.adapters.exception.ProviderNotFoundException;
import com.tienda.compraservice.infraestructure.adapters.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.List;

import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProviderServiceTest {
    @Mock
    private ProviderPersistancePort providerPersistancePort;

    @Mock
    private ProviderDomainMapper providerDomainMapper;

    @InjectMocks
    private ProviderService providerService;

    private Provider mockProvider;
    private ProviderResponse mockProviderResponse;
    private ProviderRequest mockProviderRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        providerService = new ProviderService(providerPersistancePort, providerDomainMapper);

        mockProvider = new Provider(
                UUID.randomUUID(), "Test Provider", "123 Test St", "123-456-7890",
                "test@provider.com", true);
        mockProviderResponse = new ProviderResponse(mockProvider.getId(), mockProvider.getName(),
                mockProvider.getAddress(), mockProvider.getPhone(), mockProvider.getEmail(), mockProvider.getActive());
        mockProviderRequest = new ProviderRequest(mockProvider.getName(), mockProvider.getAddress(),
                mockProvider.getPhone(), mockProvider.getEmail(), mockProvider.getActive());
    }

    @Test
    void getAllProviders_ShouldReturnListOfProviders() {
        List<Provider> mockProviders = Arrays.asList(mockProvider);
        when(providerPersistancePort.findAllProviders()).thenReturn(mockProviders);
        when(providerDomainMapper.domainToResponse(mockProvider)).thenReturn(mockProviderResponse);

        List<ProviderResponse> result = providerService.getAllProviders();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Provider", result.get(0).getName());
    }

    @Test
    void getById_ShouldReturnProviderWhenFound() {
        when(providerPersistancePort.readById(mockProvider.getId())).thenReturn(mockProvider);
        when(providerDomainMapper.domainToResponse(mockProvider)).thenReturn(mockProviderResponse);

        ProviderResponse result = providerService.getById(mockProvider.getId());

        assertNotNull(result);
        assertEquals(mockProvider.getName(), result.getName());
    }

    @Test
    void getById_ShouldThrowProviderNotFoundExceptionWhenNotFound() {
        UUID nonExistentId = UUID.randomUUID();
        when(providerPersistancePort.readById(nonExistentId)).thenReturn(null);

        assertThrows(ProviderNotFoundException.class, () -> providerService.getById(nonExistentId));
    }

    @Test
    void createEntity_ShouldCreateProvider() {
        when(providerDomainMapper.reqToDomain(mockProviderRequest)).thenReturn(mockProvider);
        when(providerPersistancePort.create(mockProvider)).thenReturn(mockProvider);
        when(providerDomainMapper.domainToResponse(mockProvider)).thenReturn(mockProviderResponse);

        ProviderResponse result = providerService.createEntity(mockProviderRequest);

        assertNotNull(result);
        assertEquals("Test Provider", result.getName());
    }

    @Test
    void createEntity_ShouldThrowValidationExceptionWhenProviderExists() {
        when(providerPersistancePort.findProviderByName(mockProviderRequest.getName().toLowerCase())).thenReturn(mockProvider);

        assertThrows(ValidationException.class, () -> providerService.createEntity(mockProviderRequest));
    }

    @Test
    void updateEntity_ShouldThrowProviderNotFoundExceptionWhenProviderNotFound() {
        UUID nonExistentId = UUID.randomUUID();
        when(providerPersistancePort.readById(nonExistentId)).thenReturn(null);
        assertThrows(ProviderNotFoundException.class, () -> providerService.updateEntity(mockProviderRequest, nonExistentId));
    }

    @Test
    void deleteEntityById_ShouldDeleteProvider() {
        UUID providerId = mockProvider.getId();
        when(providerPersistancePort.readById(providerId)).thenReturn(mockProvider);
        doNothing().when(providerPersistancePort).deleteById(providerId);
        providerService.deleteEntityById(providerId);
        verify(providerPersistancePort, times(1)).deleteById(providerId);
    }
    @Test
    void deleteEntityById_ShouldThrowProviderNotFoundExceptionWhenNotFound() {
        UUID nonExistentId = UUID.randomUUID();
        when(providerPersistancePort.readById(nonExistentId)).thenReturn(null);

        assertThrows(ProviderNotFoundException.class, () -> providerService.deleteEntityById(nonExistentId));
    }
}