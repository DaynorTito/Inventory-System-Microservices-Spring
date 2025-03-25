package com.tienda.salieservice.infrastructure.adapters.adaptersimpl;

import com.tienda.salieservice.domain.model.dto.SaleDetails;
import com.tienda.salieservice.infrastructure.adapters.entity.SaleDetailsEntity;
import com.tienda.salieservice.infrastructure.adapters.exception.SaleDetailNotFound;
import com.tienda.salieservice.infrastructure.adapters.mapper.SaleDetailsMapper;
import com.tienda.salieservice.infrastructure.adapters.repository.SaleDetailsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class SaleDetailsPersistanceAdapterTest {

    @Mock
    private SaleDetailsRepository repository;

    @Mock
    private SaleDetailsMapper mapper;

    @InjectMocks
    private SaleDetailsPersistanceAdapter adapter;

    private SaleDetails saleDetails;
    private SaleDetailsEntity saleDetailsEntity;

    @BeforeEach
    void setUp() {
        saleDetails = SaleDetails.builder()
                .id(1L)
                .productId("P001")
                .quantity(5)
                .unitPrice(BigDecimal.valueOf(100.00))
                .subtotal(BigDecimal.valueOf(500.00))
                .build();

        saleDetailsEntity = SaleDetailsEntity.builder()
                .id(1L)
                .productId("P001")
                .quantity(5)
                .unitPrice(BigDecimal.valueOf(100.00))
                .subtotal(BigDecimal.valueOf(500.00))
                .build();
    }

    @Test
    void readById_ShouldReturnSaleDetails_WhenFound() {
        when(repository.findById(1L)).thenReturn(Optional.of(saleDetailsEntity));
        when(mapper.toDomain(saleDetailsEntity)).thenReturn(saleDetails);

        SaleDetails result = adapter.readById(1L);

        assertNotNull(result);
        assertEquals(saleDetails.getId(), result.getId());
        verify(repository).findById(1L);
    }

    @Test
    void readById_ShouldThrowException_WhenNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(SaleDetailNotFound.class, () -> adapter.readById(1L));
        verify(repository).findById(1L);
    }

    @Test
    void create_ShouldSaveAndReturnSaleDetails() {
        when(mapper.toEntity(saleDetails)).thenReturn(saleDetailsEntity);
        when(repository.save(saleDetailsEntity)).thenReturn(saleDetailsEntity);
        when(mapper.toDomain(saleDetailsEntity)).thenReturn(saleDetails);

        SaleDetails result = adapter.create(saleDetails);

        assertNotNull(result);
        assertEquals(saleDetails.getId(), result.getId());
        verify(repository).save(saleDetailsEntity);
    }

    @Test
    void update_ShouldModifyAndReturnSaleDetails_WhenFound() {
        when(repository.findById(1L)).thenReturn(Optional.of(saleDetailsEntity));
        when(mapper.toEntity(saleDetails)).thenReturn(saleDetailsEntity);
        when(repository.save(saleDetailsEntity)).thenReturn(saleDetailsEntity);
        when(mapper.toDomain(saleDetailsEntity)).thenReturn(saleDetails);

        SaleDetails result = adapter.update(saleDetails, 1L);

        assertNotNull(result);
        assertEquals(saleDetails.getId(), result.getId());
        verify(repository).save(saleDetailsEntity);
    }

    @Test
    void update_ShouldThrowException_WhenNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(SaleDetailNotFound.class, () -> adapter.update(saleDetails, 1L));
        verify(repository).findById(1L);
    }

    @Test
    void deleteById_ShouldDelete_WhenFound() {
        when(repository.findById(1L)).thenReturn(Optional.of(saleDetailsEntity));

        adapter.deleteById(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void deleteById_ShouldThrowException_WhenNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(SaleDetailNotFound.class, () -> adapter.deleteById(1L));
        verify(repository).findById(1L);
    }
}
