package com.tienda.productoservice.application.services;

import com.tienda.productoservice.application.mapper.CategoryDomainMapper;
import com.tienda.productoservice.domain.model.dto.Category;
import com.tienda.productoservice.domain.model.dto.request.CategoryRequest;
import com.tienda.productoservice.domain.model.dto.response.CategoryResponse;
import com.tienda.productoservice.domain.port.CategoryPersistancePort;
import com.tienda.productoservice.application.useCases.CategoryUseCases;
import com.tienda.productoservice.infrastructure.adapters.exception.CategoryNotFoundException;
import com.tienda.productoservice.infrastructure.adapters.exception.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryService implements CategoryUseCases {

    private final CategoryPersistancePort categoryPersistancePort;
    private final CategoryDomainMapper categoryMapper;

    /**
     * Retrieves all categories from the persistence layer and maps them to CategoryResponse DTOs
     *
     * @return a list of all CategoryResponse DTOs
     */
    public List<CategoryResponse> getAllCategories() {
        var categories = categoryPersistancePort.findAllCategories();
        return categories.stream()
                .map(categoryMapper::domaintoResponse)
                .toList();
    }

    /**
     * Retrieves a category by its ID, validates its existence, and maps it to a CategoryResponse DTO
     *
     * @param aLong the ID of the category to retrieve
     * @return the mapped CategoryResponse DTO for the category
     */
    @Override
    public CategoryResponse getById(Long aLong) {
        var category = validateExistCategory(aLong);
        return categoryMapper.domaintoResponse(category);
    }

    /**
     * Creates a new category based on the provided CategoryRequest
     *
     * @param request the CategoryRequest DTO containing the data for the new category
     * @return the mapped CategoryResponse DTO for the created category
     */
    @Override
    public CategoryResponse createEntity(CategoryRequest request) {
        validateExistCategory(request.getName());
        var categoryCreated = categoryPersistancePort.create(categoryMapper.reqToDomain(request));
        return categoryMapper.domaintoResponse(categoryCreated);
    }

    /**
     * Updates an existing category based on the provided CategoryRequest and ID
     *
     * @param request the CategoryRequest DTO containing the updated data for the category
     * @param aLong the ID of the category to update
     * @return the mapped CategoryResponse DTO for the updated category
     */
    @Override
    public CategoryResponse updateEntity(CategoryRequest request, Long aLong) {
        validateExistCategory(aLong);
        var categoryUpdated = categoryPersistancePort.update(categoryMapper.reqToDomain(request), aLong);
        return categoryMapper.domaintoResponse(categoryUpdated);
    }

    /**
     * Deletes a category by its ID after validating that the category exists
     *
     * @param aLong the ID of the category to delete
     */
    @Override
    public void deleteEntityById(Long aLong) {
        validateExistCategory(aLong);
        categoryPersistancePort.deleteById(aLong);
    }

    /**
     * Validates that a category exists by checking its ID
     *
     * @param id the ID of the category to validate
     * @return the category if found
     * @throws CategoryNotFoundException if the category with the given ID is not found
     */
    public Category validateExistCategory(Long id) {
        var category = categoryPersistancePort.readById(id);
        if (category == null) {
            throw new CategoryNotFoundException("Categoria no encontrada");
        }
        return category;
    }

    /**
     * Validates that a category does not already exist by checking its name
     *
     * @param name the name of the category to validate
     * @throws ValidationException if a category with the given name already exists
     */
    public void validateExistCategory(String name) {
        var category = categoryPersistancePort.findCategoryByName(name.trim().toLowerCase());
        if (category != null) {
            throw new ValidationException("Categoria ya existe");
        }
    }
}
