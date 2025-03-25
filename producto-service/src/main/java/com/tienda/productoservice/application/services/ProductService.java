package com.tienda.productoservice.application.services;

import com.tienda.productoservice.application.mapper.ProductDomainMapper;
import com.tienda.productoservice.domain.model.dto.Brand;
import com.tienda.productoservice.domain.model.dto.Category;
import com.tienda.productoservice.domain.model.dto.Product;
import com.tienda.productoservice.domain.model.dto.request.ProductRequest;
import com.tienda.productoservice.domain.model.dto.response.ProductResponse;
import com.tienda.productoservice.domain.port.BrandPersistancePort;
import com.tienda.productoservice.domain.port.CategoryPersistancePort;
import com.tienda.productoservice.domain.port.ProductPersistancePort;
import com.tienda.productoservice.application.useCases.ProductUseCases;
import com.tienda.productoservice.infrastructure.adapters.exception.ProductNotFoundException;
import com.tienda.productoservice.infrastructure.adapters.exception.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductService implements ProductUseCases {

    private final ProductPersistancePort productPersistancePort;
    private final CategoryPersistancePort categoryPersistancePort;
    private final BrandPersistancePort brandPersistancePort;
    private final ProductDomainMapper productMapper;

    /**
     * Retrieve all products
     *
     * @return List of ProductResponse objects
     */
    public List<ProductResponse> getAllProducts() {
        var products = productPersistancePort.getAllProducts();
        return products.stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Retrieve a product by its code
     *
     * @param cod Product code
     * @return ProductResponse object
     */
    @Override
    public ProductResponse getById(String cod) {
        var product = validateExistProduct(cod);
        return productMapper.toResponse(product);
    }

    /**
     * Create a new product entity
     *
     * @param request ProductRequest object containing product details
     * @return ProductResponse object with created product details
     */
    @Override
    public ProductResponse createEntity(ProductRequest request) {
        var cod = createProductCod(request);
        var newProduct = productMapper.toDomainFromReq(request);
        newProduct.setCod(cod);
        newProduct.setCategory(validateExistCategory(request.getCategory()));
        newProduct.setBrand(validateExistBrand(request.getBrand()));
        newProduct.setCreationDate(LocalDateTime.now());
        if (request.getDiscount() != null && newProduct.getDiscount().compareTo(newProduct.getSalePrice()) > 0)
            throw new ValidationException("El descuento no debe ser mayor que el precio de venta");
        var product = productPersistancePort.create(newProduct);
        return productMapper.toResponse(product);
    }

    /**
     * Update an existing product entity
     *
     * @param request ProductRequest object containing updated product details
     * @param cod Product code
     * @return ProductResponse object with updated product details
     */
    @Override
    public ProductResponse updateEntity(ProductRequest request, String cod) {
        validateExistProduct(cod);
        var product = productPersistancePort.update(productMapper.toDomainFromReq(request), cod);
        return productMapper.toResponse(product);
    }

    /**
     * Delete a product entity by its code.
     *
     * @param cod Product code
     */
    @Override
    public void deleteEntityById(String cod) {
        validateExistProduct(cod);
        productPersistancePort.deleteById(cod);
    }

    /**
     * Generate a product code based on the product request
     *
     * @param productRequest ProductRequest object
     * @return Generated product code
     */
    @Override
    public String createProductCod(ProductRequest productRequest) {
        try {
            String productName = productRequest.getName().trim().toLowerCase();
            String prefix = generatePrefix(productRequest);
            String hash = generateHash(productName).substring(0, 4);
            return prefix + hash;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al generar el código de producto", e);
        }
    }

    /**
     * Search products based on category, brand, and price range.
     *
     * @param category Product category
     * @param brand Product brand
     * @param minPrice Minimum price range
     * @param maxPrice Maximum price range
     * @return List of ProductResponse objects matching the search criteria
     */
    @Override
    public List<ProductResponse> findAllByCategoryBrandAndPrice(
            String category, String brand, BigDecimal minPrice, BigDecimal maxPrice) {

        var products = productPersistancePort.getAllProducts();

        if (category != null && !category.isEmpty()) {
            products = products.stream()
                    .filter(p -> category.equalsIgnoreCase(p.getCategory().getName()))
                    .toList();
        }

        if (brand != null && !brand.isEmpty()) {
            products = products.stream()
                    .filter(p -> brand.equalsIgnoreCase(p.getBrand().getName()))
                    .toList();
        }

        BigDecimal min = minPrice != null ? minPrice : BigDecimal.ZERO;
        BigDecimal max = maxPrice != null ? maxPrice : BigDecimal.valueOf(Double.MAX_VALUE);

        products = products.stream()
                .filter(p -> p.getSalePrice().compareTo(min) >= 0 && p.getSalePrice().compareTo(max) <= 0)
                .toList();

        return products.stream().map(productMapper::toResponse).collect(Collectors.toList());
    }

    /**
     * Search products by a word (e.g., product name or description)
     *
     * @param word Search term
     * @return List of ProductResponse objects matching the search term
     */
    @Override
    public List<ProductResponse> findAllByWord(String word) {
        if (word == null || word.trim().isEmpty())
            throw new ValidationException("La palabra de busqueda no puede estar vacía");
        var products = productPersistancePort.findAllByWord(word.trim().toLowerCase());
        return products.stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Validate if a product exists by its code
     *
     * @param cod Product code
     * @return Product entity if found
     * @throws ProductNotFoundException if product is not found
     */
    public Product validateExistProduct(String cod) {
        var product = productPersistancePort.readById(cod);
        if (product == null) {
            throw new ProductNotFoundException("Producto no encontrado con Cod: " + cod);
        }
        return product;
    }

    /**
     * Generate a product code prefix from the product name and category.
     *
     * @param productRequest ProductRequest object
     * @return Generated prefix
     */
    private String generatePrefix(ProductRequest productRequest) {
        String productName = productRequest.getName().toUpperCase();
        String category = productRequest.getCategory().toUpperCase();
        String prefixFromName = productName.length() >= 2 ? productName.substring(0, 2) : productName;
        String prefixFromCategory = category.length() >= 2 ? category.substring(0, 2) : category;
        return prefixFromName + prefixFromCategory;
    }

    /**
     * Generate a SHA-256 hash for a given input string.
     *
     * @param input Input string
     * @return Generated hash string
     * @throws NoSuchAlgorithmException if the hashing algorithm is not available
     */
    private String generateHash(String input) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
        return hashBytesToHex(hashBytes);
    }

    /**
     * Convert a byte array into a hexadecimal string.
     *
     * @param hashBytes Byte array
     * @return Hexadecimal string representation of the byte array
     */
    private String hashBytesToHex(byte[] hashBytes) {
        return java.util.stream.IntStream.range(0, hashBytes.length)
                .mapToObj(i -> String.format("%02x", hashBytes[i]))
                .collect(Collectors.joining());
    }

    /**
     * Validate if a category exists.
     *
     * @param category Category name
     * @return Category entity if found
     * @throws ValidationException if category is not found
     */
    private Category validateExistCategory(String category) {
        var categoryClean = category.trim().toLowerCase();
        var  categoryFound = categoryPersistancePort.findCategoryByName(categoryClean);
        if (categoryFound == null) {
            throw new ValidationException("Categoria no encontrada");
        }
        return categoryFound;
    }

    /**
     * Validate if a brand exists.
     *
     * @param brand Brand name
     * @return Brand entity if found
     * @throws ValidationException if brand is not found
     */
    private Brand validateExistBrand(String brand) {
        var brandClean = brand.trim().toLowerCase();
        var  brandFound = brandPersistancePort.findBrandByName(brandClean);
        if (brandFound == null) {
            throw new ValidationException("Marca no encontrada");
        }
        return brandFound;
    }
}
