package com.tienda.compraservice.application.services;


import com.tienda.compraservice.application.mapper.ProviderDomainMapper;
import com.tienda.compraservice.domain.model.dto.Provider;
import com.tienda.compraservice.domain.model.dto.request.ProviderRequest;
import com.tienda.compraservice.domain.model.dto.response.ProviderResponse;
import com.tienda.compraservice.domain.port.ProviderPersistancePort;
import com.tienda.compraservice.application.useCases.ProviderUseCases;
import com.tienda.compraservice.infraestructure.adapters.exception.ProviderNotFoundException;

import com.tienda.compraservice.infraestructure.adapters.exception.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ProviderService implements ProviderUseCases {

    private final ProviderPersistancePort providerPersistancePort;
    private final ProviderDomainMapper providerDomainMapper;

    /**
     * Retrieves all providers
     *
     * @return A list of all provider responses
     */
    public List<ProviderResponse> getAllProviders() {
        var providers = providerPersistancePort.findAllProviders();
        return providers.stream()
                .map(providerDomainMapper::domainToResponse)
                .toList();
    }

    /**
     * Retrieves a provider by its ID
     *
     * @param uuid The ID of the provider
     * @return The provider response
     */
    @Override
    public ProviderResponse getById(UUID uuid) {
        var provider = validateExistProviderId(uuid);
        return providerDomainMapper.domainToResponse(provider);
    }

    /**
     * Creates a new provider
     *
     * @param request The provider request
     * @return The created provider response
     */
    @Override
    public ProviderResponse createEntity(ProviderRequest request) {
        validateExistProvider(request.getName());
        if (request.getActive() == null) request.setActive(true);
        var providerCreated = providerPersistancePort.create(providerDomainMapper.reqToDomain(request));
        return providerDomainMapper.domainToResponse(providerCreated);
    }

    /**
     * Updates an existing provider
     *
     * @param request The provider request with updated data
     * @param uuid The ID of the provider to update
     * @return The updated provider response
     */
    @Override
    public ProviderResponse updateEntity(ProviderRequest request, UUID uuid) {
        validateExistProviderId(uuid);
        var providerUpdated = providerPersistancePort.update(providerDomainMapper.reqToDomain(request), uuid);
        return providerDomainMapper.domainToResponse(providerUpdated);
    }

    /**
     * Deletes a provider by its ID
     *
     * @param uuid The ID of the provider to delete
     */
    @Override
    public void deleteEntityById(UUID uuid) {
        validateExistProviderId(uuid);
        providerPersistancePort.deleteById(uuid);
    }

    /**
     * Validates if a provider exists by its ID
     *
     * @param uuid The ID of the provider to validate
     * @return The provider object
     * @throws ProviderNotFoundException if the provider does not exist
     */
    private Provider validateExistProviderId(UUID uuid) {
        var provider = providerPersistancePort.readById(uuid);
        if (provider == null) {
            throw new ProviderNotFoundException("Proveedor no encontrado");
        }
        return provider;
    }

    /**
     * Validates if a provider already exists by its name
     *
     * @param name The name of the provider
     * @throws ValidationException if the provider already exists
     */
    private void validateExistProvider(String name) {
        var provider = providerPersistancePort.findProviderByName(name.trim().toLowerCase());
        if (provider != null) {
            throw new ValidationException("Proveedor ya existe");
        }
    }
}
