package com.tienda.productoservice.infrastructure.adapters.adapterimpl;

import com.tienda.productoservice.domain.model.dto.Category;
import com.tienda.productoservice.infrastructure.adapters.entity.CategoryEntity;
import com.tienda.productoservice.infrastructure.adapters.exception.CategoryNotFoundException;
import com.tienda.productoservice.infrastructure.adapters.mapper.CategoryMapper;
import com.tienda.productoservice.infrastructure.adapters.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryPersistanceAdapterTest {
    @InjectMocks
    private CategoryPersistanceAdapter categoryPersistanceAdapter;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    private CategoryEntity categoryEntity;
    private Category category;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        categoryEntity = new CategoryEntity(1L, "Electronics", "All electronic products", null);
        category = new Category(1L, "Electronics", "All electronic products");
    }

    @Test
    void readById_ShouldReturnCategory() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(categoryEntity));
        when(categoryMapper.toDomain(categoryEntity)).thenReturn(category);

        Category result = categoryPersistanceAdapter.readById(1L);

        assertNotNull(result);
        assertEquals(category.getId(), result.getId());
        assertEquals(category.getName(), result.getName());
        assertEquals(category.getDescription(), result.getDescription());

        verify(categoryRepository, times(1)).findById(1L);
        verify(categoryMapper, times(1)).toDomain(categoryEntity);
    }

    @Test
    void readById_ShouldReturnNullWhenCategoryNotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        Category result = categoryPersistanceAdapter.readById(1L);

        assertNull(result);

        verify(categoryRepository, times(1)).findById(1L);
    }

    @Test
    void create_ShouldReturnCreatedCategory() {
        when(categoryMapper.toEntity(category)).thenReturn(categoryEntity);
        when(categoryRepository.save(categoryEntity)).thenReturn(categoryEntity);
        when(categoryMapper.toDomain(categoryEntity)).thenReturn(category);

        Category result = categoryPersistanceAdapter.create(category);

        assertNotNull(result);
        assertEquals(category.getId(), result.getId());
        assertEquals(category.getName(), result.getName());
        assertEquals(category.getDescription(), result.getDescription());

        verify(categoryMapper, times(1)).toEntity(category);
        verify(categoryRepository, times(1)).save(categoryEntity);
        verify(categoryMapper, times(1)).toDomain(categoryEntity);
    }

    @Test
    void update_ShouldReturnUpdatedCategory() {
        Category updatedCategory = new Category(1L, "Electronics Updated", "Updated Description");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(categoryEntity));
        when(categoryMapper.toEntity(updatedCategory)).thenReturn(categoryEntity);
        when(categoryRepository.save(categoryEntity)).thenReturn(categoryEntity);
        when(categoryMapper.toDomain(categoryEntity)).thenReturn(updatedCategory);

        Category result = categoryPersistanceAdapter.update(updatedCategory, 1L);

        assertNotNull(result);
        assertEquals(updatedCategory.getId(), result.getId());
        assertEquals(updatedCategory.getName(), result.getName());
        assertEquals(updatedCategory.getDescription(), result.getDescription());

        verify(categoryRepository, times(1)).findById(1L);
        verify(categoryRepository, times(1)).save(categoryEntity);
        verify(categoryMapper, times(1)).toEntity(updatedCategory);
        verify(categoryMapper, times(1)).toDomain(categoryEntity);
    }

    @Test
    void update_ShouldThrowCategoryNotFoundExceptionWhenCategoryDoesNotExist() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> categoryPersistanceAdapter.update(category, 1L));

        verify(categoryRepository, times(1)).findById(1L);
    }

    @Test
    void deleteById_ShouldDeleteCategory() {
        categoryPersistanceAdapter.deleteById(1L);

        verify(categoryRepository, times(1)).deleteById(1L);
    }

    @Test
    void findCategoryByName_ShouldReturnCategory() {
        when(categoryRepository.findByName("Electronics")).thenReturn(Optional.of(categoryEntity));
        when(categoryMapper.toDomain(categoryEntity)).thenReturn(category);

        Category result = categoryPersistanceAdapter.findCategoryByName("Electronics");

        assertNotNull(result);
        assertEquals(category.getId(), result.getId());
        assertEquals(category.getName(), result.getName());
        assertEquals(category.getDescription(), result.getDescription());

        verify(categoryRepository, times(1)).findByName("Electronics");
        verify(categoryMapper, times(1)).toDomain(categoryEntity);
    }

    @Test
    void findAllCategories_ShouldReturnListOfCategories() {
        when(categoryRepository.findAll()).thenReturn(List.of(categoryEntity));
        when(categoryMapper.toDomain(categoryEntity)).thenReturn(category);

        List<Category> result = categoryPersistanceAdapter.findAllCategories();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(category.getId(), result.get(0).getId());

        verify(categoryRepository, times(1)).findAll();
        verify(categoryMapper, times(1)).toDomain(categoryEntity);
    }
}