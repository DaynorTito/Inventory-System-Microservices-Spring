package com.tienda.compraservice.infraestructure.adapters.mapper;


import com.tienda.compraservice.domain.model.dto.Purchase;
import com.tienda.compraservice.infraestructure.adapters.entity.PurchaseEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PurchaseMapper {

    /**
     * Maps a PurchaseEntity to a Purchase domain object
     *
     * @param provider The purchase entity
     * @return The mapped purchase domain object
     */
    Purchase toDomain(PurchaseEntity provider);
}
