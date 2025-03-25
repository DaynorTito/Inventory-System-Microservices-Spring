package com.tienda.productoservice.infrastructure.adapters.mapper;

import com.tienda.productoservice.domain.model.dto.Product;
import com.tienda.productoservice.infrastructure.adapters.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    /**
     * Convert ProductEntity to Product
     *
     * @param product the product entity
     * @return the product domain object
     */
    @Mapping(source = "cod", target = "cod")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "category", target = "category")
    @Mapping(source = "brand", target = "brand")
    @Mapping(source = "salePrice", target = "salePrice")
    @Mapping(source = "creationDate", target = "creationDate")
    @Mapping(source = "discount", target = "discount")
    Product toDomain(ProductEntity product);

    /**
     * Convert Product to ProductEntity
     *
     * @param product the product domain object
     * @return the product entity
     */
    @Mapping(source = "cod", target = "cod")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "category", target = "category")
    @Mapping(source = "brand", target = "brand")
    @Mapping(source = "salePrice", target = "salePrice")
    @Mapping(source = "discount", target = "discount")
    ProductEntity toEntity(Product product);
}
