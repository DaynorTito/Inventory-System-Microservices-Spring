package com.tienda.productoservice.application.mapper;

import com.tienda.productoservice.domain.model.dto.Brand;
import com.tienda.productoservice.domain.model.dto.Category;
import com.tienda.productoservice.domain.model.dto.Product;
import com.tienda.productoservice.domain.model.dto.request.ProductRequest;
import com.tienda.productoservice.domain.model.dto.response.ProductResponse;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.context.annotation.Primary;

@Mapper(componentModel = "spring")
@Primary
public interface ProductDomainMapper {

    /**
     * Maps a Product domain object to a ProductResponse DTO.
     *
     * @param product the Product domain object to be mapped.
     * @return the mapped ProductResponse DTO.
     */
    @Mapping(source = "cod", target = "cod")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "category", target = "category")
    @Mapping(source = "brand", target = "brand")
    @Mapping(source = "salePrice", target = "salePrice")
    @Mapping(source = "creationDate", target = "creationDate")
    @Mapping(source = "discount", target = "discount")
    ProductResponse toResponse(Product product);

    /**
     * Maps a ProductResponse DTO back to a Product domain object
     *
     * @param product the ProductResponse DTO to be mapped
     * @return the mapped Product domain object
     */
    @InheritInverseConfiguration
    Product toDomain(ProductResponse product);

    /**
     * Maps a ProductRequest DTO to a Product domain object
     *
     * @param request the ProductRequest DTO to be mapped
     * @return the mapped Product domain object
     */
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "category", target = "category")
    @Mapping(source = "brand", target = "brand")
    @Mapping(source = "salePrice", target = "salePrice")
    @Mapping(source = "discount", target = "discount")
    Product toDomainFromReq(ProductRequest request);

    /**
     * Maps a string value to a Category domain object
     *
     * @param value the name of the category
     * @return the mapped Category domain object
     */
    default Category map(String value) {
        return Category.builder()
                .name(value)
                .build();
    }

    /**
     * Maps a string value to a Brand domain object
     *
     * @param value the name of the brand
     * @return the mapped Brand domain object
     */
    default Brand mapBrand(String value) {
        return Brand.builder()
                .name(value)
                .build();
    }
}
