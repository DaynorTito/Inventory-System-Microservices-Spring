package com.tienda.compraservice.infraestructure.adapters.adapterimpl;

import com.tienda.compraservice.domain.model.dto.Provider;
import com.tienda.compraservice.domain.port.ProviderPersistancePort;
import com.tienda.compraservice.infraestructure.adapters.exception.ProviderNotFoundException;
import com.tienda.compraservice.infraestructure.adapters.mapper.ProviderMapper;
import com.tienda.compraservice.infraestructure.adapters.repository.ProviderRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class ProviderPersistanceAdapter implements ProviderPersistancePort {

    private final ProviderRepository providerRepository;
    private final ProviderMapper providerMapper;

    /**
     * Finds a provider by its name
     *
     * @param providerName The name of the provider
     * @return The provider found or null if not found
     */
    @Override
    public Provider findProviderByName(String providerName) {
        var provider = providerRepository.findByName(providerName);
        return provider.map(providerMapper::toDomain).orElse(null);
    }

    /**
     * Finds all providers
     *
     * @return A list of all providers
     */
    @Override
    public List<Provider> findAllProviders() {
        return providerRepository.findAll()
                .stream()
                .map(providerMapper::toDomain)
                .toList();
    }

    /**
     * Finds a provider by its ID
     *
     * @param uuid The UUID of the provider
     * @return The provider found or null if not found
     */
    @Override
    public Provider readById(UUID uuid) {
        var provider = providerRepository.findById(uuid);
        return provider.map(providerMapper::toDomain).orElse(null);
    }

    /**
     * Creates a new provider
     *
     * @param request The provider to create
     * @return The created provider
     */
    @Override
    public Provider create(Provider request) {
        var providerToSave = providerMapper.toEntity(request);
        var providerSaved = providerRepository.save(providerToSave);
        return providerMapper.toDomain(providerSaved);
    }

    /**
     * Updates an existing provider
     *
     * @param request The provider details to update
     * @param uuid The UUID of the provider to update
     * @return The updated provider
     */
    @Override
    public Provider update(Provider request, UUID uuid) {
        var providerToUpdate = providerRepository.findById(uuid).orElseThrow(
                () -> new ProviderNotFoundException("Proveedor no encontrada"));
        var requestEntity = providerMapper.toEntity(request);
        UpdateHelper.updateNonNullFields(requestEntity, providerToUpdate);
        var providerUpdated = providerRepository.save(providerToUpdate);
        return providerMapper.toDomain(providerUpdated);
    }

    /**
     * Deletes a provider by its ID
     *
     * @param uuid The UUID of the provider to delete
     */
    @Override
    public void deleteById(UUID uuid) {
        providerRepository.deleteById(uuid);
    }
}
