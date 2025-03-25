package com.tienda.productoservice.infrastructure.adapters.adapterimpl;

import com.tienda.productoservice.domain.model.dto.Brand;
import com.tienda.productoservice.domain.model.dto.Category;
import com.tienda.productoservice.domain.model.dto.Product;
import com.tienda.productoservice.infrastructure.adapters.entity.BrandEntity;
import com.tienda.productoservice.infrastructure.adapters.entity.CategoryEntity;
import com.tienda.productoservice.infrastructure.adapters.entity.ProductEntity;
import com.tienda.productoservice.infrastructure.adapters.repository.BrandRepository;
import com.tienda.productoservice.infrastructure.adapters.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

class UpdateHelperTest {
    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private BrandRepository brandRepository;

    private Product mockProduct;
    private ProductEntity mockProductEntity;
    private CategoryEntity mockCategoryEntity;
    private BrandEntity mockBrandEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Category mockCategory = new Category(1L, "TestCategory", "description");
        Brand mockBrand = new Brand(1L, "TestBrand", "description");

        mockCategoryEntity = new CategoryEntity(1L, "TestCategory", "description", null);
        mockBrandEntity = new BrandEntity(1L,"TestBrand","description", null);

        mockProduct = new Product("123", "Test Product", "Test Description",
                mockCategory, mockBrand, null, null, null);
        mockProductEntity = new ProductEntity("123", "Old Product", "Old Description",
                null, null, null, null, null);
    }

    @Test
    void updateNonNullFields_ShouldUpdateProductFieldsWhenNotNull() {
        mockProduct.setName("Updated Product");
        mockProduct.setDescription("Updated Description");

        UpdateHelper.updateNonNullFields(mockProduct, mockProductEntity);

        assertEquals("Updated Product", mockProductEntity.getName());
        assertEquals("Updated Description", mockProductEntity.getDescription());
    }

    @Test
    void updateObjectsFields_ShouldUpdateCategoryAndBrandWhenValid() {
        when(categoryRepository.findByName("TestCategory")).thenReturn(Optional.of(mockCategoryEntity));
        when(brandRepository.findByName("TestBrand")).thenReturn(Optional.of(mockBrandEntity));

        UpdateHelper.updateObjectsFields(mockProduct, categoryRepository, brandRepository, mockProductEntity);

        assertEquals(mockCategoryEntity, mockProductEntity.getCategory());
        assertEquals(mockBrandEntity, mockProductEntity.getBrand());
        verify(categoryRepository, times(1)).findByName("TestCategory");
        verify(brandRepository, times(1)).findByName("TestBrand");
    }
}