package com.tienda.productoservice.infrastructure.adapters.adapterimpl;

import com.tienda.productoservice.domain.model.dto.Product;
import com.tienda.productoservice.infrastructure.adapters.entity.BrandEntity;
import com.tienda.productoservice.infrastructure.adapters.entity.CategoryEntity;
import com.tienda.productoservice.infrastructure.adapters.entity.ProductEntity;
import com.tienda.productoservice.infrastructure.adapters.exception.BrandNotFoundException;
import com.tienda.productoservice.infrastructure.adapters.exception.CategoryNotFoundException;
import com.tienda.productoservice.infrastructure.adapters.repository.BrandRepository;
import com.tienda.productoservice.infrastructure.adapters.repository.CategoryRepository;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;

public class UpdateHelper {

    /**
     * Update non-null fields of the target object with values from the source object
     *
     * @param source the source object
     * @param target the target object
     */
    public static void updateNonNullFields(Object source, Object target) {
        BeanWrapper sourceWrapper = new BeanWrapperImpl(source);
        BeanWrapper targetWrapper = new BeanWrapperImpl(target);

        for (PropertyDescriptor descriptor : sourceWrapper.getPropertyDescriptors()) {
            String propertyName = descriptor.getName();
            if (!"class".equals(propertyName) && !"category".equals(propertyName) && !"brand".equals(propertyName)) {
                Object sourceValue = sourceWrapper.getPropertyValue(propertyName);
                if (sourceValue != null) {
                    targetWrapper.setPropertyValue(propertyName, sourceValue);
                }
            }
        }
    }

    /**
     * Update category and brand fields of the product entity using the provided repositories
     *
     * @param request the product request object
     * @param categoryRepository the category repository
     * @param brandRepository the brand repository
     * @param productToUpdate the product entity to update
     */
    public static void updateObjectsFields(Product request, CategoryRepository categoryRepository,
                                           BrandRepository brandRepository,
                                           ProductEntity productToUpdate) {
        if (request.getCategory() != null && request.getCategory().getName() != null) {
            String categoryName = request.getCategory().getName();
            CategoryEntity categoryEntity = categoryRepository.findByName(categoryName)
                    .orElseThrow(() -> new CategoryNotFoundException("Categoria no encontrada"));
            productToUpdate.setCategory(categoryEntity);
        }
        if (request.getBrand() != null && request.getBrand().getName() != null) {
            String brandName = request.getBrand().getName();
            BrandEntity brandEntity = brandRepository.findByName(brandName)
                    .orElseThrow(() -> new BrandNotFoundException("Marca no encontrada"));
            productToUpdate.setBrand(brandEntity);
        }
    }
}