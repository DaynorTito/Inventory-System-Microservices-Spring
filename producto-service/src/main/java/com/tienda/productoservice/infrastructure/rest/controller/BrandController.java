package com.tienda.productoservice.infrastructure.rest.controller;


import com.tienda.productoservice.application.services.BrandService;
import com.tienda.productoservice.domain.model.dto.request.BrandRequest;
import com.tienda.productoservice.domain.model.dto.response.BrandResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/brand")
@AllArgsConstructor
public class BrandController {
    private final BrandService brandService;

    /**
     * Fetch all brands from the service
     *
     * @return ResponseEntity containing a list of all BrandResponse objects
     */
    @GetMapping
    public ResponseEntity<List<BrandResponse>> getBrands() {
        var brandResponse = brandService.getAllBrands();
        return ResponseEntity.ok(brandResponse);
    }

    /**
     * Fetch a brand by its ID
     *
     * @param id the ID of the brand to fetch
     * @return ResponseEntity containing the BrandResponse object corresponding to the provided ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<BrandResponse> getBrandById(@PathVariable Long id) {
        var brandResponse = brandService.getById(id);
        return ResponseEntity.ok(brandResponse);
    }

    /**
     * Create a new brand based on the provided request data
     *
     * @param brandRequest the data used to create a new brand
     * @return ResponseEntity containing the created BrandResponse object with status CREATED
     */
    @PostMapping
    public ResponseEntity<BrandResponse> createBrand(@RequestBody @Valid BrandRequest brandRequest) {
        var brandResponse = brandService.createEntity(brandRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(brandResponse);
    }

    /**
     * Update an existing brand based on the provided ID and request data
     *
     * @param id the ID of the brand to update
     * @param brandRequest the data to update the brand with
     * @return ResponseEntity containing the updated BrandResponse object
     */
    @PutMapping("/{id}")
    public ResponseEntity<BrandResponse> updateBrand(@PathVariable Long id, @RequestBody BrandRequest brandRequest) {
        var brandResponse = brandService.updateEntity(brandRequest, id);
        return ResponseEntity.ok(brandResponse);
    }

    /**
     * Delete a brand by its ID
     *
     * @param id the ID of the brand to delete
     * @return ResponseEntity with status NO_CONTENT indicating successful deletion
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBrand(@PathVariable Long id) {
        brandService.deleteEntityById(id);
        return ResponseEntity.noContent().build();
    }
}
