package com.tienda.productoservice.application.mapper;

import com.tienda.productoservice.domain.model.dto.Brand;
import com.tienda.productoservice.domain.model.dto.request.BrandRequest;
import com.tienda.productoservice.domain.model.dto.response.BrandResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.context.annotation.Primary;

@Mapper(componentModel = "spring")
@Primary
public interface BrandDomainMapper {

    /**
     * Maps a Brand domain object to a BrandResponse DTO
     *
     * @param brand the Brand domain object to be mapped
     * @return the mapped BrandResponse DTO
     */
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    BrandResponse domaintoResponse(Brand brand);

    /**
     * Maps a BrandRequest DTO to a Brand domain object
     *
     * @param brandRequest the BrandRequest DTO to be mapped
     * @return the mapped Brand domain object
     */
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    Brand reqToDomain(BrandRequest brandRequest);
}
