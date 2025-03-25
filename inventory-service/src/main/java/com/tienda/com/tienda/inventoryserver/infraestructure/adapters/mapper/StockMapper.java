package com.tienda.com.tienda.inventoryserver.infraestructure.adapters.mapper;

import com.tienda.com.tienda.inventoryserver.domain.model.dto.Stock;
import com.tienda.com.tienda.inventoryserver.infraestructure.adapters.entity.StockEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StockMapper {

    /**
     * Method to map a StockEntity to a Stock domain object
     *
     * @param stockEntity StockEntity to be mapped to a Stock domain object
     * @return Mapped Stock domain object
     */
    @Mapping(source = "id", target = "id")
    @Mapping(source = "quantity", target = "quantity")
    @Mapping(source = "purchaseUnitCost", target = "purchaseUnitCost")
    @Mapping(source = "totalPurchaseCost", target = "totalPurchaseCost")
    @Mapping(source = "providerId", target = "providerId")
    @Mapping(source = "productId", target = "productId")
    @Mapping(source = "purchaseDate", target = "purchaseDate")
    @Mapping(source = "expiryDate", target = "expiryDate")
    Stock toDomain(StockEntity stockEntity);

    /**
     * Method to map a Stock domain object to a StockEntity
     *
     * @param stock Stock domain object to be mapped to a StockEntity
     * @return Mapped StockEntity
     */
    @Mapping(source = "quantity", target = "quantity")
    @Mapping(source = "purchaseUnitCost", target = "purchaseUnitCost")
    @Mapping(source = "totalPurchaseCost", target = "totalPurchaseCost")
    @Mapping(source = "providerId", target = "providerId")
    @Mapping(source = "productId", target = "productId")
    @Mapping(source = "purchaseDate", target = "purchaseDate")
    @Mapping(source = "expiryDate", target = "expiryDate")
    StockEntity toEntity(Stock stock);
}
