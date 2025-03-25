package com.tienda.productoservice.infrastructure.adapters.adapterimpl;

import com.tienda.productoservice.domain.model.dto.Brand;
import com.tienda.productoservice.infrastructure.adapters.entity.BrandEntity;
import com.tienda.productoservice.infrastructure.adapters.exception.BrandNotFoundException;
import com.tienda.productoservice.infrastructure.adapters.mapper.BrandMapper;
import com.tienda.productoservice.infrastructure.adapters.repository.BrandRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BrandPersistanceAdapterTest {

    @InjectMocks
    private BrandPersistanceAdapter brandPersistanceAdapter;

    @Mock
    private BrandRepository brandRepository;

    @Mock
    private BrandMapper brandMapper;

    private BrandEntity brandEntity;
    private Brand brand;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        brandEntity = new BrandEntity(1L, "Dell", "Leading Electronics Brand", null);
        brand = new Brand(1L, "Dell", "Leading Electronics Brand");
    }

    @Test
    void readById_ShouldReturnBrand() {
        when(brandRepository.findById(1L)).thenReturn(Optional.of(brandEntity));
        when(brandMapper.toDomain(brandEntity)).thenReturn(brand);

        Brand result = brandPersistanceAdapter.readById(1L);

        assertNotNull(result);
        assertEquals(brand.getId(), result.getId());
        assertEquals(brand.getName(), result.getName());
        assertEquals(brand.getDescription(), result.getDescription());

        verify(brandRepository, times(1)).findById(1L);
        verify(brandMapper, times(1)).toDomain(brandEntity);
    }

    @Test
    void readById_ShouldReturnNullWhenBrandNotFound() {
        when(brandRepository.findById(1L)).thenReturn(Optional.empty());

        Brand result = brandPersistanceAdapter.readById(1L);

        assertNull(result);
        verify(brandRepository, times(1)).findById(1L);
    }

    @Test
    void create_ShouldReturnCreatedBrand() {
        when(brandMapper.toEntity(brand)).thenReturn(brandEntity);
        when(brandRepository.save(brandEntity)).thenReturn(brandEntity);
        when(brandMapper.toDomain(brandEntity)).thenReturn(brand);

        Brand result = brandPersistanceAdapter.create(brand);

        assertNotNull(result);
        assertEquals(brand.getId(), result.getId());
        assertEquals(brand.getName(), result.getName());
        assertEquals(brand.getDescription(), result.getDescription());

        verify(brandMapper, times(1)).toEntity(brand);
        verify(brandRepository, times(1)).save(brandEntity);
        verify(brandMapper, times(1)).toDomain(brandEntity);
    }

    @Test
    void update_ShouldReturnUpdatedBrand() {
        Brand updatedBrand = new Brand(1L, "Dell Updated", "Updated Brand Description");

        when(brandRepository.findById(1L)).thenReturn(Optional.of(brandEntity));
        when(brandMapper.toEntity(updatedBrand)).thenReturn(brandEntity);
        when(brandRepository.save(brandEntity)).thenReturn(brandEntity);
        when(brandMapper.toDomain(brandEntity)).thenReturn(updatedBrand);

        Brand result = brandPersistanceAdapter.update(updatedBrand, 1L);

        assertNotNull(result);
        assertEquals(updatedBrand.getId(), result.getId());
        assertEquals(updatedBrand.getName(), result.getName());
        assertEquals(updatedBrand.getDescription(), result.getDescription());

        verify(brandRepository, times(1)).findById(1L);
        verify(brandRepository, times(1)).save(brandEntity);
        verify(brandMapper, times(1)).toEntity(updatedBrand);
        verify(brandMapper, times(1)).toDomain(brandEntity);
    }

    @Test
    void update_ShouldThrowBrandNotFoundException() {
        when(brandRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BrandNotFoundException.class, () -> brandPersistanceAdapter.update(brand, 1L));

        verify(brandRepository, times(1)).findById(1L);
    }

    @Test
    void deleteById_ShouldInvokeDelete() {
        brandPersistanceAdapter.deleteById(1L);

        verify(brandRepository, times(1)).deleteById(1L);
    }

    @Test
    void findBrandByName_ShouldReturnBrand() {
        when(brandRepository.findByName("Dell")).thenReturn(Optional.of(brandEntity));
        when(brandMapper.toDomain(brandEntity)).thenReturn(brand);

        Brand result = brandPersistanceAdapter.findBrandByName("Dell");

        assertNotNull(result);
        assertEquals(brand.getId(), result.getId());
        assertEquals(brand.getName(), result.getName());
        assertEquals(brand.getDescription(), result.getDescription());
        verify(brandRepository, times(1)).findByName("Dell");
        verify(brandMapper, times(1)).toDomain(brandEntity);
    }

    @Test
    void findAllBrands_ShouldReturnListOfBrands() {
        when(brandRepository.findAll()).thenReturn(List.of(brandEntity));
        when(brandMapper.toDomain(brandEntity)).thenReturn(brand);

        List<Brand> result = brandPersistanceAdapter.findAllBrands();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(brand.getId(), result.get(0).getId());

        verify(brandRepository, times(1)).findAll();
        verify(brandMapper, times(1)).toDomain(brandEntity);
    }
}