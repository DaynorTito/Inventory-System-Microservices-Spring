package com.tienda.productoservice.application.services;

import com.tienda.productoservice.application.mapper.BrandDomainMapper;
import com.tienda.productoservice.domain.model.dto.Brand;
import com.tienda.productoservice.domain.model.dto.request.BrandRequest;
import com.tienda.productoservice.domain.model.dto.response.BrandResponse;
import com.tienda.productoservice.domain.port.BrandPersistancePort;
import com.tienda.productoservice.infrastructure.adapters.exception.BrandNotFoundException;
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
class BrandServiceTest {
    @Mock
    private BrandPersistancePort brandPersistancePort;

    @Mock
    private BrandDomainMapper brandMapper;

    @InjectMocks
    private BrandService brandService;

    private Brand brand;
    private BrandRequest brandRequest;
    private BrandResponse brandResponse;

    @BeforeEach
    void setUp() {
        brand = new Brand(1L, "Nike", "Sports brand");
        brandRequest = new BrandRequest("Nike", "Sports brand");
        brandResponse = new BrandResponse(1L, "Nike", "Sports brand");
    }

    @Test
    void testGetAllBrands() {
        when(brandPersistancePort.findAllBrands()).thenReturn(List.of(brand));
        when(brandMapper.domaintoResponse(any())).thenReturn(brandResponse);

        List<BrandResponse> brands = brandService.getAllBrands();

        assertEquals(1, brands.size());
        assertEquals("Nike", brands.get(0).getName());
        verify(brandPersistancePort).findAllBrands();
    }

    @Test
    void testGetByIdSuccess() {
        when(brandPersistancePort.readById(1L)).thenReturn(brand);
        when(brandMapper.domaintoResponse(brand)).thenReturn(brandResponse);

        BrandResponse response = brandService.getById(1L);

        assertNotNull(response);
        assertEquals("Nike", response.getName());
    }

    @Test
    void testGetByIdNotFound() {
        when(brandPersistancePort.readById(1L)).thenReturn(null);
        assertThrows(BrandNotFoundException.class, () -> brandService.getById(1L));
    }

    @Test
    void testCreateEntitySuccess() {
        when(brandPersistancePort.findBrandByName(any())).thenReturn(null);
        when(brandMapper.reqToDomain(brandRequest)).thenReturn(brand);
        when(brandPersistancePort.create(brand)).thenReturn(brand);
        when(brandMapper.domaintoResponse(brand)).thenReturn(brandResponse);

        BrandResponse response = brandService.createEntity(brandRequest);
        assertNotNull(response);
        assertEquals("Nike", response.getName());
    }

    @Test
    void testCreateEntityAlreadyExists() {
        when(brandPersistancePort.findBrandByName(any())).thenReturn(brand);
        assertThrows(ValidationException.class, () -> brandService.createEntity(brandRequest));
    }

    @Test
    void testUpdateEntitySuccess() {
        when(brandPersistancePort.readById(1L)).thenReturn(brand);
        when(brandMapper.reqToDomain(brandRequest)).thenReturn(brand);
        when(brandPersistancePort.update(brand, 1L)).thenReturn(brand);
        when(brandMapper.domaintoResponse(brand)).thenReturn(brandResponse);

        BrandResponse response = brandService.updateEntity(brandRequest, 1L);
        assertNotNull(response);
        assertEquals("Nike", response.getName());
    }

    @Test
    void testUpdateEntityNotFound() {
        when(brandPersistancePort.readById(1L)).thenReturn(null);
        assertThrows(BrandNotFoundException.class, () -> brandService.updateEntity(brandRequest, 1L));
    }

    @Test
    void testDeleteEntityByIdSuccess() {
        when(brandPersistancePort.readById(1L)).thenReturn(brand);
        doNothing().when(brandPersistancePort).deleteById(1L);

        assertDoesNotThrow(() -> brandService.deleteEntityById(1L));
    }

    @Test
    void testDeleteEntityByIdNotFound() {
        when(brandPersistancePort.readById(1L)).thenReturn(null);
        assertThrows(BrandNotFoundException.class, () -> brandService.deleteEntityById(1L));
    }
}