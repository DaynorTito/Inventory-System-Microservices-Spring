package com.tienda.com.tienda.inventoryserver.infraestructure.adapters.adapterimpl;

import com.tienda.com.tienda.inventoryserver.domain.model.constant.TypeMove;
import com.tienda.com.tienda.inventoryserver.domain.model.dto.Kardex;
import com.tienda.com.tienda.inventoryserver.domain.model.dto.TopSellingProduct;
import com.tienda.com.tienda.inventoryserver.infraestructure.adapters.entity.KardexEntity;
import com.tienda.com.tienda.inventoryserver.infraestructure.adapters.exception.KardexNotFoundException;
import com.tienda.com.tienda.inventoryserver.infraestructure.adapters.mapper.KardexMapper;
import com.tienda.com.tienda.inventoryserver.infraestructure.adapters.repository.KardexRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KardexPersistanceAdapterTest {

    @Mock
    private KardexRepository kardexRepository;

    @Mock
    private KardexMapper kardexMapper;

    @InjectMocks
    private KardexPersistanceAdapter kardexPersistanceAdapter;

    private KardexEntity kardexEntity;
    private Kardex kardex;

    @BeforeEach
    public void setup() {
        UUID uuid = UUID.randomUUID();
        LocalDate movementDate = LocalDate.now();
        kardexEntity = new KardexEntity(uuid, TypeMove.OUTCOME, 10, "prod123",
                BigDecimal.valueOf(100), BigDecimal.valueOf(1000), movementDate);
        kardex = new Kardex(uuid, TypeMove.OUTCOME, 10, "prod123", BigDecimal.valueOf(100),
                BigDecimal.valueOf(1000), movementDate);
    }

    @Test
    public void findAllKardexByProductId_ShouldReturnKardexList() {
        when(kardexRepository.findAllByProductIdOrderByMovementDateDesc("prod123"))
                .thenReturn(Collections.singletonList(kardexEntity));
        when(kardexMapper.toDomain(kardexEntity)).thenReturn(kardex);
        List<Kardex> result = kardexPersistanceAdapter.findAllKardexByProductId("prod123");
        assertEquals(1, result.size());
        assertEquals(kardex, result.getFirst());
        verify(kardexRepository, times(1)).findAllByProductIdOrderByMovementDateDesc("prod123");
        verify(kardexMapper, times(1)).toDomain(kardexEntity);
    }

    @Test
    public void findTopSellingProducts_ShouldReturnTopSellingProducts() {
        LocalDate after = LocalDate.now().minusMonths(1);
        LocalDate before = LocalDate.now();
        int limit = 5;
        List<Object[]> mockResults = Arrays.asList(new Object[][] {
                {"prod123", 50}
        });
        when(kardexRepository.findTopSellingProducts(after, before, PageRequest.of(0, limit)))
                .thenReturn(mockResults);
        List<TopSellingProduct> result = kardexPersistanceAdapter.findTopSellingProducts(after, before, limit);
        assertEquals(1, result.size());
        assertEquals("prod123", result.getFirst().getProductId());
        assertEquals(50, result.getFirst().getTotalSold());
        verify(kardexRepository, times(1)).findTopSellingProducts(after, before,
                PageRequest.of(0, limit));
    }

    @Test
    public void findAllKardexByMovementDateBetween_ShouldReturnKardexList() {
        LocalDate after = LocalDate.now().minusDays(5);
        LocalDate before = LocalDate.now();
        when(kardexRepository.findAllByMovementDateBetween(after, before))
                .thenReturn(Collections.singletonList(kardexEntity));
        when(kardexMapper.toDomain(kardexEntity)).thenReturn(kardex);
        List<Kardex> result = kardexPersistanceAdapter.findAllKardexByMovementDateBetween(after, before);
        assertEquals(1, result.size());
        assertEquals(kardex, result.getFirst());
        verify(kardexRepository, times(1)).findAllByMovementDateBetween(after, before);
        verify(kardexMapper, times(1)).toDomain(kardexEntity);
    }

    @Test
    public void calculateEarningsBetweenDates_ShouldReturnEarnings() {
        LocalDate startDate = LocalDate.now().minusMonths(1);
        LocalDate endDate = LocalDate.now();
        when(kardexRepository.calculateEarningsBetweenDates(startDate, endDate))
                .thenReturn(BigDecimal.valueOf(5000));
        BigDecimal result = kardexPersistanceAdapter.calculateEarningsBetweenDates(startDate, endDate);
        assertEquals(BigDecimal.valueOf(5000), result);
        verify(kardexRepository, times(1)).calculateEarningsBetweenDates(startDate, endDate);
    }

    @Test
    public void getLastKardexByProductId_ShouldReturnKardex() {
        when(kardexRepository.findFirstByProductIdOrderByMovementDateDesc("prod123"))
                .thenReturn(kardexEntity);
        when(kardexMapper.toDomain(kardexEntity)).thenReturn(kardex);
        Kardex result = kardexPersistanceAdapter.getLastKardexByProductId("prod123");
        assertEquals(kardex, result);
        verify(kardexRepository, times(1)).findFirstByProductIdOrderByMovementDateDesc("prod123");
        verify(kardexMapper, times(1)).toDomain(kardexEntity);
    }

    @Test
    public void readById_ShouldReturnKardex() {
        UUID uuid = UUID.randomUUID();
        when(kardexRepository.findById(uuid)).thenReturn(Optional.of(kardexEntity));
        when(kardexMapper.toDomain(kardexEntity)).thenReturn(kardex);
        Kardex result = kardexPersistanceAdapter.readById(uuid);
        assertEquals(kardex, result);
        verify(kardexRepository, times(1)).findById(uuid);
        verify(kardexMapper, times(1)).toDomain(kardexEntity);
    }

    @Test
    public void readById_ShouldThrowKardexNotFoundException() {
        UUID uuid = UUID.randomUUID();
        when(kardexRepository.findById(uuid)).thenReturn(Optional.empty());
        assertThrows(KardexNotFoundException.class, () -> kardexPersistanceAdapter.readById(uuid));
        verify(kardexRepository, times(1)).findById(uuid);
    }

    @Test
    public void create_ShouldReturnCreatedKardex() {
        when(kardexMapper.toEntity(kardex)).thenReturn(kardexEntity);
        when(kardexRepository.save(kardexEntity)).thenReturn(kardexEntity);
        when(kardexMapper.toDomain(kardexEntity)).thenReturn(kardex);
        Kardex result = kardexPersistanceAdapter.create(kardex);
        assertEquals(kardex, result);
        verify(kardexMapper, times(1)).toEntity(kardex);
        verify(kardexRepository, times(1)).save(kardexEntity);
        verify(kardexMapper, times(1)).toDomain(kardexEntity);
    }

    @Test
    public void update_ShouldReturnUpdatedKardex() {
        UUID uuid = UUID.randomUUID();
        when(kardexRepository.findById(uuid)).thenReturn(Optional.of(kardexEntity));
        when(kardexMapper.toEntity(kardex)).thenReturn(kardexEntity);
        when(kardexRepository.save(kardexEntity)).thenReturn(kardexEntity);
        when(kardexMapper.toDomain(kardexEntity)).thenReturn(kardex);
        Kardex result = kardexPersistanceAdapter.update(kardex, uuid);
        assertEquals(kardex, result);
        verify(kardexRepository, times(1)).findById(uuid);
        verify(kardexMapper, times(1)).toEntity(kardex);
        verify(kardexRepository, times(1)).save(kardexEntity);
        verify(kardexMapper, times(1)).toDomain(kardexEntity);
    }

    @Test
    public void deleteById_ShouldDeleteKardex() {
        UUID uuid = UUID.randomUUID();
        doNothing().when(kardexRepository).deleteById(uuid);
        kardexPersistanceAdapter.deleteById(uuid);
        verify(kardexRepository, times(1)).deleteById(uuid);
    }
}
