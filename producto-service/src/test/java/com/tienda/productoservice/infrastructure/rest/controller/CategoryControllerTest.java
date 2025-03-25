package com.tienda.productoservice.infrastructure.rest.controller;

import com.tienda.productoservice.application.services.CategoryService;
import com.tienda.productoservice.domain.model.dto.request.CategoryRequest;
import com.tienda.productoservice.domain.model.dto.response.CategoryResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {
    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    @Test
    void getCategories_ShouldReturnCategories() {
        List<CategoryResponse> categoryResponses = List.of(new CategoryResponse(1L, "Electronics", "Electronic Items"));
        when(categoryService.getAllCategories()).thenReturn(categoryResponses);

        ResponseEntity<List<CategoryResponse>> response = categoryController.getCategories();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getCategoryById_ShouldReturnCategory() {
        CategoryResponse categoryResponse = new CategoryResponse(1L, "Electronics", "Electronic Items");
        when(categoryService.getById(1L)).thenReturn(categoryResponse);

        ResponseEntity<CategoryResponse> response = categoryController.getCategoryById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Electronics", response.getBody().getName());
    }

    @Test
    void createCategory_ShouldCreateCategory() {
        CategoryRequest categoryRequest = new CategoryRequest("Electronics", "Electronic Items");
        CategoryResponse categoryResponse = new CategoryResponse(1L, "Electronics", "Electronic Items");
        when(categoryService.createEntity(categoryRequest)).thenReturn(categoryResponse);

        ResponseEntity<CategoryResponse> response = categoryController.createCategory(categoryRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Electronics", response.getBody().getName());
    }

    @Test
    void updateCategory_ShouldUpdateCategory() {
        CategoryRequest categoryRequest = new CategoryRequest("Electronics", "Updated Description");
        CategoryResponse categoryResponse = new CategoryResponse(1L, "Electronics", "Updated Description");
        when(categoryService.updateEntity(categoryRequest, 1L)).thenReturn(categoryResponse);

        ResponseEntity<CategoryResponse> response = categoryController.updateCategory(1L, categoryRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Electronics", response.getBody().getName());
    }

    @Test
    void deleteCategory_ShouldDeleteCategory() {
        doNothing().when(categoryService).deleteEntityById(1L);

        ResponseEntity<Void> response = categoryController.deleteCategory(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(categoryService, times(1)).deleteEntityById(1L);
    }
}