package com.tienda.productoservice.infrastructure.rest.controller;


import com.tienda.productoservice.application.services.ProductService;
import com.tienda.productoservice.domain.model.dto.request.ProductRequest;
import com.tienda.productoservice.domain.model.dto.response.ProductResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/product")
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;

    /**
     * Fetch all products from the service
     *
     * @return ResponseEntity containing a list of all ProductResponse objects
     */
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getProducts() {
        var productResponse = productService.getAllProducts();
        return ResponseEntity.ok(productResponse);
    }

    /**
     * Fetch a product by its code
     *
     * @param cod the code of the product to fetch
     * @return ResponseEntity containing the ProductResponse object corresponding to the provided code
     */
    @GetMapping("/{cod}")
    public ResponseEntity<ProductResponse> getProductByCod(@PathVariable String cod) {
        var productResponse = productService.getById(cod);
        return ResponseEntity.ok(productResponse);
    }

    /**
     * Create a new product based on the provided request data
     *
     * @param productRequest the data used to create a new product
     * @return ResponseEntity containing the created ProductResponse object with status CREATED
     */
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody @Valid ProductRequest productRequest) {
        var productResponse = productService.createEntity(productRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(productResponse);
    }

    /**
     * Update an existing product based on the provided code and request data
     *
     * @param cod the code of the product to update
     * @param productRequest the data to update the product with
     * @return ResponseEntity containing the updated ProductResponse object
     */
    @PutMapping("/{cod}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable String cod, @RequestBody ProductRequest productRequest) {
        var productResponse = productService.updateEntity(productRequest, cod);
        return ResponseEntity.ok(productResponse);
    }

    /**
     * Delete a product by its code
     *
     * @param cod the code of the product to delete
     * @return ResponseEntity with status NO_CONTENT indicating successful deletion
     */
    @DeleteMapping("/{cod}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String cod) {
        productService.deleteEntityById(cod);
        return ResponseEntity.noContent().build();
    }

    /**
     * Fetch products filtered by category, brand, and price range
     *
     * @param category the category of the product (optional)
     * @param brand the brand of the product (optional)
     * @param minPrice the minimum price (optional)
     * @param maxPrice the maximum price (optional)
     * @return ResponseEntity containing a list of ProductResponse objects filtered by the given criteria
     */
    @GetMapping("/category-brand-price")
    public ResponseEntity<List<ProductResponse>> getProductsByCategoryBrandAndPrice(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice) {
        var products = productService.findAllByCategoryBrandAndPrice(category, brand, minPrice, maxPrice);
        return ResponseEntity.ok(products);
    }

    /**
     * Search for products by a keyword
     *
     * @param word the keyword to search for in product names or descriptions
     * @return ResponseEntity containing a list of ProductResponse objects matching the search term
     */
    @GetMapping("/search")
    public ResponseEntity<List<ProductResponse>> getProductsByWord(@RequestParam String word) {
        var products = productService.findAllByWord(word);
        return ResponseEntity.ok(products);
    }
}
