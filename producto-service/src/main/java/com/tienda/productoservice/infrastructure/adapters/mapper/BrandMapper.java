package com.tienda.productoservice.infrastructure.adapters.mapper;


import com.tienda.productoservice.domain.model.dto.Brand;
import com.tienda.productoservice.infrastructure.adapters.entity.BrandEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BrandMapper {

    /**
     * Convert BrandEntity to Brand
     *
     * @param brand the brand entity
     * @return the brand domain object
     */
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    Brand toDomain(BrandEntity brand);

    /**
     * Convert Brand to BrandEntity
     *
     * @param brand the brand domain object
     * @return the brand entity
     */
    @InheritInverseConfiguration
    BrandEntity toEntity(Brand brand);
}
