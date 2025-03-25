package com.tienda.compraservice.domain.port;

import com.tienda.compraservice.domain.abstractions.PersistancePort;
import com.tienda.compraservice.domain.model.dto.Provider;

import java.util.List;
import java.util.UUID;

public interface ProviderPersistancePort extends PersistancePort<Provider, UUID> {
    Provider findProviderByName(String providerName);
    List<Provider> findAllProviders();
}
