package com.tienda.com.tienda.inventoryserver.infraestructure.adapters.mapper;

import com.tienda.com.tienda.inventoryserver.domain.model.dto.Kardex;
import com.tienda.com.tienda.inventoryserver.infraestructure.adapters.entity.KardexEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface KardexMapper {

    /**
     * Method to map a KardexEntity to a Kardex domain object
     *
     * @param kardex KardexEntity to be mapped to a Kardex domain object
     * @return Mapped Kardex domain object
     */
    @Mapping(source = "id", target = "id")
    @Mapping(source = "typeMovement", target = "typeMovement")
    @Mapping(source = "productId", target = "productId")
    @Mapping(source = "quantity", target = "quantity")
    @Mapping(source = "unitPrice", target = "unitPrice")
    @Mapping(source = "totalPrice", target = "totalPrice")
    @Mapping(source = "movementDate", target = "movementDate")
    Kardex toDomain(KardexEntity kardex);

    /**
     * Method to map a Kardex domain object to a KardexEntity
     *
     * @param kardex Kardex domain object to be mapped to a KardexEntity
     * @return Mapped KardexEntity
     */
    @Mapping(source = "typeMovement", target = "typeMovement")
    @Mapping(source = "productId", target = "productId")
    @Mapping(source = "quantity", target = "quantity")
    @Mapping(source = "unitPrice", target = "unitPrice")
    @Mapping(source = "totalPrice", target = "totalPrice")
    @Mapping(source = "movementDate", target = "movementDate")
    KardexEntity toEntity(Kardex kardex);
}
