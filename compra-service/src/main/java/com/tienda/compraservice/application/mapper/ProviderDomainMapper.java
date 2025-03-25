package com.tienda.compraservice.application.mapper;

import com.tienda.compraservice.domain.model.dto.Provider;
import com.tienda.compraservice.domain.model.dto.request.ProviderRequest;
import com.tienda.compraservice.domain.model.dto.response.ProviderResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProviderDomainMapper {

    /**
     * Converts a Provider domain entity to a ProviderResponse DTO
     *
     * @param provider the provider entity to convert
     * @return a ProviderResponse DTO containing the mapped data
     */
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "active", target = "active")
    ProviderResponse domainToResponse(Provider provider);

    /**
     * Converts a ProviderRequest DTO to a Provider domain entity
     *
     * @param providerRequest the DTO containing provider data
     * @return a Provider entity with the mapped data
     */
    @Mapping(source = "name", target = "name")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "active", target = "active")
    Provider reqToDomain(ProviderRequest providerRequest);
}
