package com.tienda.productoservice.infrastructure.adapters.adapterimpl;

import com.tienda.productoservice.domain.model.dto.Product;
import com.tienda.productoservice.domain.port.ProductPersistancePort;
import com.tienda.productoservice.infrastructure.adapters.exception.ProductNotFoundException;
import com.tienda.productoservice.infrastructure.adapters.mapper.ProductMapper;
import com.tienda.productoservice.infrastructure.adapters.repository.BrandRepository;
import com.tienda.productoservice.infrastructure.adapters.repository.CategoryRepository;
import com.tienda.productoservice.infrastructure.adapters.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class ProductPersistanceAdapter implements ProductPersistancePort {


    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;

    /**
     * Get a product by its ID
     *
     * @param id the ID of the product to retrieve
     * @return the found product, or null if not found
     */
    @Override
    public Product readById(String id) {
        var product = productRepository.findById(id);
        return product.map(productMapper::toDomain).orElse(null);
    }

    /**
     * Create a new product in the database
     *
     * @param request the product to create
     * @return the created product
     */
    @Override
    public Product create(Product request) {
        var productToSave = productMapper.toEntity(request);
        var productSaved = productRepository.save(productToSave);
        return productMapper.toDomain(productSaved);
    }

    /**
     * Update an existing product in the database
     *
     * @param request the new information for the product
     * @param id the ID of the product to update
     * @return the updated product
     */
    @Override
    public Product update(Product request, String id) {
        var productToUpdate = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Producto no encontrado"));
        var productEntity = productMapper.toEntity(request);
        UpdateHelper.updateNonNullFields(productEntity, productToUpdate);
        UpdateHelper.updateObjectsFields(request, categoryRepository, brandRepository, productToUpdate);
        var productUpdated = productRepository.save(productToUpdate);
        return productMapper.toDomain(productUpdated);
    }

    /**
     * Delete a product from the database by its ID
     *
     * @param s the ID of the product to delete
     */
    @Override
    public void deleteById(String s) {
        productRepository.deleteById(s);
    }

    /**
     * Get all products from the database
     *
     * @return a list of all products
     */
    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll().stream()
                .map(productMapper::toDomain)
                .collect(Collectors.toList());
    }

    /**
     * Get all products by category and brand
     *
     * @param category the category of the products
     * @param brand the brand of the products
     * @return a list of products matching the category and brand
     */
    @Override
    public List<Product> findAllByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrandName(category, brand).stream()
                .map(productMapper::toDomain)
                .collect(Collectors.toList());
    }

    /**
     * Search for products by name or description containing a specific word
     *
     * @param word the word to search for
     * @return a list of products matching the search criteria
     */
    @Override
    public List<Product> findAllByWord(String word) {
        return productRepository
                .findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(word, word).stream()
                .map(productMapper::toDomain)
                .collect(Collectors.toList());
    }
}
