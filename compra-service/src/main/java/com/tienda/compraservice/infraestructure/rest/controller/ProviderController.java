package com.tienda.compraservice.infraestructure.rest.controller;

import com.tienda.compraservice.application.services.ProviderService;
import com.tienda.compraservice.domain.model.dto.request.ProviderRequest;
import com.tienda.compraservice.domain.model.dto.response.ProviderResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/provider")
@AllArgsConstructor
public class ProviderController {

    private final ProviderService providerService;

    /**
     * Fetch a list of all providers
     *
     * @return A response containing the list of all providers
     */
    @GetMapping
    public ResponseEntity<List<ProviderResponse>> getProviders() {
        var providers = providerService.getAllProviders();
        return ResponseEntity.ok(providers);
    }

    /**
     * Fetch a provider by its ID
     *
     * @param id The ID of the provider
     * @return A response containing the provider details
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProviderResponse> getProviderById(@PathVariable UUID id) {
        var providerResponse = providerService.getById(id);
        return ResponseEntity.ok(providerResponse);
    }

    /**
     * Creates a new provider
     *
     * @param providerRequest The provider request object containing provider details
     * @return A response containing the created provider details
     */
    @PostMapping
    public ResponseEntity<ProviderResponse> createProvider(@RequestBody @Valid ProviderRequest providerRequest) {
        var providerResponse = providerService.createEntity(providerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(providerResponse);
    }

    /**
     * Updates an existing provider
     *
     * @param id The ID of the provider to update
     * @param providerRequest The provider request object containing updated details
     * @return A response containing the updated provider details
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProviderResponse> updateProvider(@PathVariable UUID id, @RequestBody ProviderRequest providerRequest) {
        var providerResponse = providerService.updateEntity(providerRequest, id);
        return ResponseEntity.ok(providerResponse);
    }

    /**
     * Deletes a provider by its ID
     *
     * @param id The ID of the provider to delete
     * @return A response indicating the deletion was successful
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProvider(@PathVariable UUID id) {
        providerService.deleteEntityById(id);
        return ResponseEntity.noContent().build();
    }
}
