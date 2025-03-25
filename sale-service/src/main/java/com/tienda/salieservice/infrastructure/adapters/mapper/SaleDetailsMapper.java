package com.tienda.salieservice.infrastructure.adapters.mapper;


import com.tienda.salieservice.domain.model.dto.SaleDetails;
import com.tienda.salieservice.infrastructure.adapters.entity.SaleDetailsEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.context.annotation.Primary;

@Mapper(componentModel = "spring")
@Primary
public interface SaleDetailsMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "productId", source = "productId")
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "unitPrice", source = "unitPrice")
    @Mapping(target = "subtotal", source = "subtotal")
    @Mapping(target = "sale", ignore = true)
    SaleDetails toDomain(SaleDetailsEntity saleDetailsEntity);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "productId", source = "productId")
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "unitPrice", source = "unitPrice")
    @Mapping(target = "subtotal", source = "subtotal")
    @Mapping(target = "sale", ignore = true)
    SaleDetailsEntity toEntity(SaleDetails saleDetails);
}
