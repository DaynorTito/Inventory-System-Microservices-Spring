package com.tienda.compraservice.infraestructure.adapters.mapper;

import com.tienda.compraservice.domain.model.dto.Provider;
import com.tienda.compraservice.infraestructure.adapters.entity.ProviderEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProviderMapper {

    /**
     * Maps a ProviderEntity to a Provider domain object
     *
     * @param provider The provider entity
     * @return The mapped provider domain object
     */
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "active", target = "active")
    Provider toDomain(ProviderEntity provider);

    /**
     * Maps a Provider domain object to a ProviderEntity
     *
     * @param provider The provider domain object
     * @return The mapped provider entity
     */
    @InheritInverseConfiguration
    ProviderEntity toEntity(Provider provider);
}
