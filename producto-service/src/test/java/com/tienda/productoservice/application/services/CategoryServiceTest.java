package com.tienda.productoservice.application.services;

import com.tienda.productoservice.application.mapper.CategoryDomainMapper;
import com.tienda.productoservice.domain.model.dto.Category;
import com.tienda.productoservice.domain.model.dto.request.CategoryRequest;
import com.tienda.productoservice.domain.model.dto.response.CategoryResponse;
import com.tienda.productoservice.domain.port.CategoryPersistancePort;
import com.tienda.productoservice.infrastructure.adapters.exception.CategoryNotFoundException;
import com.tienda.productoservice.infrastructure.adapters.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
    @Mock
    private CategoryPersistancePort categoryPersistancePort;

    @Mock
    private CategoryDomainMapper categoryMapper;

    @InjectMocks
    private CategoryService categoryService;

    private Category category;
    private CategoryRequest categoryRequest;
    private CategoryResponse categoryResponse;

    @BeforeEach
    void setUp() {
        category = new Category(1L, "Electronics", "Devices and gadgets");
        categoryRequest = new CategoryRequest("Electronics", "Devices and gadgets");
        categoryResponse = new CategoryResponse(1L, "Electronics", "Devices and gadgets");
    }

    @Test
    void getAllCategories_ShouldReturnList() {
        when(categoryPersistancePort.findAllCategories()).thenReturn(List.of(category));
        when(categoryMapper.domaintoResponse(category)).thenReturn(categoryResponse);

        List<CategoryResponse> responses = categoryService.getAllCategories();

        assertEquals(1, responses.size());
        assertEquals("Electronics", responses.getFirst().getName());
        verify(categoryPersistancePort, times(1)).findAllCategories();
    }

    @Test
    void getById_ShouldReturnCategory() {
        when(categoryPersistancePort.readById(1L)).thenReturn(category);
        when(categoryMapper.domaintoResponse(category)).thenReturn(categoryResponse);

        CategoryResponse response = categoryService.getById(1L);

        assertNotNull(response);
        assertEquals("Electronics", response.getName());
        verify(categoryPersistancePort, times(1)).readById(1L);
    }

    @Test
    void getById_ShouldThrowCategoryNotFoundException() {
        when(categoryPersistancePort.readById(1L)).thenReturn(null);

        assertThrows(CategoryNotFoundException.class, () -> categoryService.getById(1L));
        verify(categoryPersistancePort, times(1)).readById(1L);
    }

    @Test
    void createEntity_ShouldCreateCategory() {
        when(categoryPersistancePort.findCategoryByName("electronics")).thenReturn(null);
        when(categoryMapper.reqToDomain(categoryRequest)).thenReturn(category);
        when(categoryPersistancePort.create(category)).thenReturn(category);
        when(categoryMapper.domaintoResponse(category)).thenReturn(categoryResponse);

        CategoryResponse response = categoryService.createEntity(categoryRequest);

        assertNotNull(response);
        assertEquals("Electronics", response.getName());
        verify(categoryPersistancePort, times(1)).create(category);
    }

    @Test
    void createEntity_ShouldThrowValidationException() {
        when(categoryPersistancePort.findCategoryByName("electronics")).thenReturn(category);

        assertThrows(ValidationException.class, () -> categoryService.createEntity(categoryRequest));
        verify(categoryPersistancePort, times(1)).findCategoryByName("electronics");
    }

    @Test
    void updateEntity_ShouldUpdateCategory() {
        when(categoryPersistancePort.readById(1L)).thenReturn(category);
        when(categoryMapper.reqToDomain(categoryRequest)).thenReturn(category);
        when(categoryPersistancePort.update(category, 1L)).thenReturn(category);
        when(categoryMapper.domaintoResponse(category)).thenReturn(categoryResponse);

        CategoryResponse response = categoryService.updateEntity(categoryRequest, 1L);

        assertNotNull(response);
        assertEquals("Electronics", response.getName());
        verify(categoryPersistancePort, times(1)).update(category, 1L);
    }

    @Test
    void updateEntity_ShouldThrowCategoryNotFoundException() {
        when(categoryPersistancePort.readById(1L)).thenReturn(null);

        assertThrows(CategoryNotFoundException.class, () -> categoryService.updateEntity(categoryRequest, 1L));
        verify(categoryPersistancePort, times(1)).readById(1L);
    }

    @Test
    void deleteEntityById_ShouldDeleteCategory() {
        when(categoryPersistancePort.readById(1L)).thenReturn(category);
        doNothing().when(categoryPersistancePort).deleteById(1L);

        assertDoesNotThrow(() -> categoryService.deleteEntityById(1L));
        verify(categoryPersistancePort, times(1)).deleteById(1L);
    }

    @Test
    void deleteEntityById_ShouldThrowCategoryNotFoundException() {
        when(categoryPersistancePort.readById(1L)).thenReturn(null);

        assertThrows(CategoryNotFoundException.class, () -> categoryService.deleteEntityById(1L));
        verify(categoryPersistancePort, times(1)).readById(1L);
    }
}