package com.tienda.productoservice.infrastructure.adapters.mapper;


import com.tienda.productoservice.domain.model.dto.Category;
import com.tienda.productoservice.infrastructure.adapters.entity.CategoryEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    /**
     * Convert CategoryEntity to Category
     *
     * @param category the category entity
     * @return the category domain object
     */
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    Category toDomain(CategoryEntity category);

    /**
     * Convert Category to CategoryEntity
     *
     * @param category the category domain object
     * @return the category entity
     */
    @InheritInverseConfiguration
    CategoryEntity toEntity(Category category);
}
