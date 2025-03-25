package com.tienda.productoservice.infrastructure.adapters.adapterimpl;

import com.tienda.productoservice.domain.model.dto.Product;
import com.tienda.productoservice.infrastructure.adapters.entity.ProductEntity;
import com.tienda.productoservice.infrastructure.adapters.exception.ProductNotFoundException;
import com.tienda.productoservice.infrastructure.adapters.mapper.ProductMapper;
import com.tienda.productoservice.infrastructure.adapters.repository.BrandRepository;
import com.tienda.productoservice.infrastructure.adapters.repository.CategoryRepository;
import com.tienda.productoservice.infrastructure.adapters.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductPersistanceAdapterTest {
    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private BrandRepository brandRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductPersistanceAdapter productPersistanceAdapter;

    private Product mockProduct;
    private ProductEntity mockProductEntity;

    @BeforeEach
    void setUp() {
        mockProduct = Product.builder()
                .cod("123")
                .name("Test Product")
                .description("Test Description")
                .category(null)
                .brand(null)
                .discount(BigDecimal.valueOf(10))
                .salePrice(BigDecimal.valueOf(100))
                .creationDate(LocalDateTime.now())
                .build();

        mockProductEntity = ProductEntity.builder()
                .cod("123")
                .name("Test Product")
                .description("Test Description")
                .category(null)
                .brand(null)
                .discount(BigDecimal.valueOf(10))
                .salePrice(BigDecimal.valueOf(100))
                .creationDate(LocalDateTime.now())
                .build();
    }

    @Test
    void create_ShouldReturnCreatedProduct() {
        when(productMapper.toEntity(mockProduct)).thenReturn(mockProductEntity);
        when(productRepository.save(mockProductEntity)).thenReturn(mockProductEntity);
        when(productMapper.toDomain(mockProductEntity)).thenReturn(mockProduct);

        Product result = productPersistanceAdapter.create(mockProduct);

        assertNotNull(result);
        assertEquals(mockProduct.getCod(), result.getCod());
        verify(productRepository, times(1)).save(mockProductEntity);
    }

    @Test
    void readById_ShouldReturnProductWhenExists() {
        when(productRepository.findById("123")).thenReturn(Optional.of(mockProductEntity));
        when(productMapper.toDomain(mockProductEntity)).thenReturn(mockProduct);

        Product result = productPersistanceAdapter.readById("123");

        assertNotNull(result);
        assertEquals(mockProduct.getCod(), result.getCod());
        verify(productRepository, times(1)).findById("123");
    }

    @Test
    void readById_ShouldReturnNullWhenProductNotFound() {
        when(productRepository.findById("123")).thenReturn(Optional.empty());

        Product result = productPersistanceAdapter.readById("123");

        assertNull(result);
        verify(productRepository, times(1)).findById("123");
    }

    @Test
    void update_ShouldReturnUpdatedProduct() {
        Product updatedProduct = new Product(
                "123",
                "Updated Name",
                "Test Description",
                null,
                null,
                BigDecimal.valueOf(10),
                BigDecimal.valueOf(100),
                LocalDateTime.now()
        );
        ProductEntity updatedProductEntity = new ProductEntity(
                "123",
                "Updated Name",
                "Test Description",
                BigDecimal.valueOf(10),
                null,
                null,
                BigDecimal.valueOf(100),
                LocalDateTime.now()
        );

        when(productRepository.findById("123")).thenReturn(Optional.of(mockProductEntity));
        when(productMapper.toEntity(updatedProduct)).thenReturn(updatedProductEntity);
        when(productRepository.save(updatedProductEntity)).thenReturn(updatedProductEntity);
        when(productMapper.toDomain(updatedProductEntity)).thenReturn(updatedProduct);
        Product result = productPersistanceAdapter.update(updatedProduct, "123");
        assertNotNull(result);
        assertEquals(updatedProduct.getName(), result.getName());
        verify(productRepository, times(1)).save(updatedProductEntity);
    }

    @Test
    void update_ShouldThrowProductNotFoundExceptionWhenProductNotFound() {
        when(productRepository.findById("123")).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productPersistanceAdapter.update(mockProduct, "123"));
        verify(productRepository, times(1)).findById("123");
    }

    @Test
    void deleteById_ShouldDeleteProduct() {
        productPersistanceAdapter.deleteById("123");

        verify(productRepository, times(1)).deleteById("123");
    }

    @Test
    void findAllByCategoryAndBrand_ShouldReturnProducts() {
        List<ProductEntity> productEntities = List.of(mockProductEntity);
        when(productRepository.findByCategoryNameAndBrandName("category", "brand")).thenReturn(productEntities);
        when(productMapper.toDomain(mockProductEntity)).thenReturn(mockProduct);

        List<Product> result = productPersistanceAdapter.findAllByCategoryAndBrand("category", "brand");

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(productRepository, times(1)).findByCategoryNameAndBrandName("category", "brand");
    }

    @Test
    void findAllByWord_ShouldReturnProducts() {
        List<ProductEntity> productEntities = List.of(mockProductEntity);
        when(productRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase("Test", "Test")).thenReturn(productEntities);
        when(productMapper.toDomain(mockProductEntity)).thenReturn(mockProduct);

        List<Product> result = productPersistanceAdapter.findAllByWord("Test");

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(productRepository, times(1)).findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase("Test", "Test");
    }
}