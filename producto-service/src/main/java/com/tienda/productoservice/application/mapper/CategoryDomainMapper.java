package com.tienda.productoservice.application.mapper;


import com.tienda.productoservice.application.services.BrandService;
import com.tienda.productoservice.domain.model.dto.Category;
import com.tienda.productoservice.domain.model.dto.request.CategoryRequest;
import com.tienda.productoservice.domain.model.dto.response.CategoryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.context.annotation.Primary;

@Mapper(componentModel = "spring", uses = {BrandService.class})
@Primary
public interface CategoryDomainMapper {

    /**
     * Maps a Category domain object to a CategoryResponse DTO.
     *
     * @param category the Category domain object to be mapped.
     * @return the mapped CategoryResponse DTO.
     */
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    CategoryResponse domaintoResponse(Category category);

    /**
     * Maps a CategoryRequest DTO to a Category domain object
     *
     * @param category the CategoryRequest DTO to be mapped
     * @return the mapped Category domain object
     */
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    Category reqToDomain(CategoryRequest category);
}
