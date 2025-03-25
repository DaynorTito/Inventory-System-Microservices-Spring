package com.tienda.salieservice.infrastructure.adapters.mapper;


import com.tienda.salieservice.domain.model.dto.Sale;
import com.tienda.salieservice.infrastructure.adapters.entity.SaleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.context.annotation.Primary;

@Mapper(componentModel = "spring", uses = {SaleDetailsMapper.class})
@Primary
public interface SaleMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "saleDate", source = "saleDate")
    @Mapping(target = "totalAmount", source = "totalAmount")
    @Mapping(target = "customerName", source = "customerName")
    @Mapping(target = "paymentMethod", source = "paymentMethod")
    @Mapping(target = "totalDiscount", source = "totalDiscount")
    @Mapping(target = "saleDetails", source = "saleDetails")
    Sale toDomain(SaleEntity saleEntity);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "saleDate", source = "saleDate")
    @Mapping(target = "totalAmount", source = "totalAmount")
    @Mapping(target = "customerName", source = "customerName")
    @Mapping(target = "paymentMethod", source = "paymentMethod")
    @Mapping(target = "totalDiscount", source = "totalDiscount")
    @Mapping(target = "saleDetails", source = "saleDetails")
    SaleEntity toEntity(Sale sale);
}
