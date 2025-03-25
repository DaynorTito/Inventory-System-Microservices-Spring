package com.tienda.compraservice.application.useCases;

import com.tienda.compraservice.domain.abstractions.CrudService;
import com.tienda.compraservice.domain.model.dto.request.ProviderRequest;
import com.tienda.compraservice.domain.model.dto.response.ProviderResponse;

import java.util.UUID;

public interface ProviderUseCases extends CrudService<ProviderRequest, ProviderResponse, UUID> {
}
