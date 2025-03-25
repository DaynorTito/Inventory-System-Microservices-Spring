package com.tienda.productoservice.infrastructure.rest.controller;

import com.tienda.productoservice.application.services.CategoryService;
import com.tienda.productoservice.domain.model.dto.request.CategoryRequest;
import com.tienda.productoservice.domain.model.dto.response.CategoryResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@AllArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    /**
     * Fetch all categories from the service
     *
     * @return ResponseEntity containing a list of all CategoryResponse objects
     */
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getCategories() {
        var categoryResponse = categoryService.getAllCategories();
        return ResponseEntity.ok(categoryResponse);
    }

    /**
     * Fetch a category by its ID
     *
     * @param id the ID of the category to fetch
     * @return ResponseEntity containing the CategoryResponse object corresponding to the provided ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Long id) {
        var categoryResponse = categoryService.getById(id);
        return ResponseEntity.ok(categoryResponse);
    }

    /**
     * Create a new category based on the provided request data
     *
     * @param categoryRequest the data used to create a new category
     * @return ResponseEntity containing the created CategoryResponse object with status CREATED
     */
    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@RequestBody @Valid CategoryRequest categoryRequest) {
        var categoryResponse = categoryService.createEntity(categoryRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryResponse);
    }

    /**
     * Update an existing category based on the provided ID and request data
     *
     * @param id the ID of the category to update
     * @param categoryRequest the data to update the category with
     * @return ResponseEntity containing the updated CategoryResponse object
     */
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(@PathVariable Long id, @RequestBody CategoryRequest categoryRequest) {
        var categoryResponse = categoryService.updateEntity(categoryRequest, id);
        return ResponseEntity.ok(categoryResponse);
    }

    /**
     * Delete a category by its ID
     *
     * @param id the ID of the category to delete
     * @return ResponseEntity with status NO_CONTENT indicating successful deletion
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteEntityById(id);
        return ResponseEntity.noContent().build();
    }
}
