package com.tienda.salieservice.infrastructure.adapters.adaptersimpl;

import com.tienda.salieservice.domain.model.dto.Sale;
import com.tienda.salieservice.infrastructure.adapters.entity.SaleEntity;
import com.tienda.salieservice.infrastructure.adapters.exception.SaleNotFOund;
import com.tienda.salieservice.infrastructure.adapters.mapper.SaleMapper;
import com.tienda.salieservice.infrastructure.adapters.repository.SaleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SalePersistanceAdapterTest {
    @Mock
    private SaleRepository repository;

    @Mock
    private SaleMapper mapper;

    @InjectMocks
    private SalePersistanceAdapter adapter;

    private Sale sale;
    private SaleEntity saleEntity;
    private UUID saleId;

    @BeforeEach
    void setUp() {
        saleId = UUID.randomUUID();
        sale = Sale.builder()
                .id(saleId)
                .saleDate(LocalDateTime.now())
                .totalAmount(BigDecimal.valueOf(100.0))
                .customerName("John Doe")
                .paymentMethod("Credit Card")
                .totalDiscount(BigDecimal.valueOf(10))
                .build();

        saleEntity = SaleEntity.builder()
                .id(saleId)
                .saleDate(LocalDateTime.now())
                .totalAmount(BigDecimal.valueOf(100.0))
                .customerName("John Doe")
                .paymentMethod("Credit Card")
                .totalDiscount(BigDecimal.valueOf(10))
                .build();
    }

    @Test
    void testReadById_ShouldReturnSuccess() {
        when(repository.findById(saleId)).thenReturn(Optional.of(saleEntity));
        when(mapper.toDomain(saleEntity)).thenReturn(sale);

        Sale result = adapter.readById(saleId);

        assertNotNull(result);
        assertEquals(saleId, result.getId());
    }

    @Test
    void testReadById_ShouldReturnNotFound() {
        when(repository.findById(saleId)).thenReturn(Optional.empty());

        assertThrows(SaleNotFOund.class, () -> adapter.readById(saleId));
    }

    @Test
    void testCreate_ShouldReturnSuccess() {
        when(mapper.toEntity(sale)).thenReturn(saleEntity);
        when(repository.save(saleEntity)).thenReturn(saleEntity);
        when(mapper.toDomain(saleEntity)).thenReturn(sale);

        Sale result = adapter.create(sale);

        assertNotNull(result);
        assertEquals(saleId, result.getId());
    }

    @Test
    void testUpdate_ShouldReturnSuccess() {
        when(repository.findById(saleId)).thenReturn(Optional.of(saleEntity));
        when(mapper.toEntity(sale)).thenReturn(saleEntity);
        when(repository.save(saleEntity)).thenReturn(saleEntity);
        when(mapper.toDomain(saleEntity)).thenReturn(sale);

        Sale result = adapter.update(sale, saleId);

        assertNotNull(result);
        assertEquals(saleId, result.getId());
    }

    @Test
    void testUpdate_ShouldReturnNotFound() {
        when(repository.findById(saleId)).thenReturn(Optional.empty());

        assertThrows(SaleNotFOund.class, () -> adapter.update(sale, saleId));
    }

    @Test
    void testDeleteById_ShouldReturnSuccess() {
        when(repository.findById(saleId)).thenReturn(Optional.of(saleEntity));
        doNothing().when(repository).deleteById(saleId);

        assertDoesNotThrow(() -> adapter.deleteById(saleId));
    }

    @Test
    void testDeleteById_ShouldReturnNotFound() {
        when(repository.findById(saleId)).thenReturn(Optional.empty());

        assertThrows(SaleNotFOund.class, () -> adapter.deleteById(saleId));
    }
}
