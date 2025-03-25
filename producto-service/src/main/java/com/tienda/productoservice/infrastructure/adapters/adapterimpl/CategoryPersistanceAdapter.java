package com.tienda.productoservice.infrastructure.adapters.adapterimpl;


import com.tienda.productoservice.domain.model.dto.Category;
import com.tienda.productoservice.domain.port.CategoryPersistancePort;
import com.tienda.productoservice.infrastructure.adapters.exception.CategoryNotFoundException;
import com.tienda.productoservice.infrastructure.adapters.mapper.CategoryMapper;
import com.tienda.productoservice.infrastructure.adapters.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class CategoryPersistanceAdapter implements CategoryPersistancePort {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    /**
     * Get a category by its ID
     *
     * @param id the ID of the category to retrieve
     * @return the found category, or null if not found
     */
    @Override
    public Category readById(Long id) {
        var category = categoryRepository.findById(id);
        return category.map(categoryMapper::toDomain).orElse(null);
    }

    /**
     * Create a new category in the database
     *
     * @param request the category to create
     * @return the created category
     */
    @Override
    public Category create(Category request) {
        var categoryToSave = categoryMapper.toEntity(request);
        var categorySaved = categoryRepository.save(categoryToSave);
        return categoryMapper.toDomain(categorySaved);
    }

    /**
     * Update an existing category in the database
     *
     * @param request the new information for the category
     * @param id the ID of the category to update
     * @return the updated category
     */
    @Override
    public Category update(Category request, Long id) {
        var categoryToUpdate = categoryRepository.findById(id).orElseThrow(
                () -> new CategoryNotFoundException("Categoria no encontrada"));
        var requestEntity = categoryMapper.toEntity(request);
        UpdateHelper.updateNonNullFields(requestEntity, categoryToUpdate);
        var categoryUpdated = categoryRepository.save(categoryToUpdate);
        return categoryMapper.toDomain(categoryUpdated);
    }

    /**
     * Delete a category from the database by its ID
     *
     * @param id the ID of the category to delete
     */
    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }

    /**
     * Find a category by its name
     *
     * @param brand the name of the category to search for
     * @return the found category, or null if not found
     */
    @Override
    public Category findCategoryByName(String brand) {
        var categoryFind = categoryRepository.findByName(brand);
        return categoryFind.map(categoryMapper::toDomain).orElse(null);
    }

    /**
     * Get all categories from the database
     *
     * @return a list of all categories
     */
    @Override
    public List<Category> findAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toDomain)
                .toList();
    }
}
