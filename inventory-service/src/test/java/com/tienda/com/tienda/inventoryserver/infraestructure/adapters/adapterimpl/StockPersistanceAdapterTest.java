package com.tienda.com.tienda.inventoryserver.infraestructure.adapters.adapterimpl;

import com.tienda.com.tienda.inventoryserver.domain.model.dto.Stock;
import com.tienda.com.tienda.inventoryserver.infraestructure.adapters.entity.StockEntity;
import com.tienda.com.tienda.inventoryserver.infraestructure.adapters.exception.StockNotFoundException;
import com.tienda.com.tienda.inventoryserver.infraestructure.adapters.mapper.StockMapper;
import com.tienda.com.tienda.inventoryserver.infraestructure.adapters.repository.StockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class StockPersistanceAdapterTest {

    @Mock
    private StockRepository stockRepository;

    @Mock
    private StockMapper stockMapper;

    @InjectMocks
    private StockPersistanceAdapter stockPersistanceAdapter;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void findAllStocksByProductId_ShouldReturnStocks() {
        String productId = "prod123";
        List<Stock> mockStocks = List.of(new Stock(UUID.randomUUID(),
                10, BigDecimal.valueOf(100), BigDecimal.valueOf(1000),
                UUID.randomUUID(), productId, LocalDate.now(), LocalDate.now()));
        when(stockRepository.findAllByProductIdOrderByExpiryDateAsc(productId)).thenReturn(List.of(
                new StockEntity(UUID.randomUUID(), 10, BigDecimal.valueOf(100), BigDecimal.valueOf(1000),
                        UUID.randomUUID(), productId, LocalDate.now(), LocalDate.now())));
        when(stockMapper.toDomain(any(StockEntity.class))).thenReturn(mockStocks.getFirst());
        List<Stock> result = stockPersistanceAdapter.findAllStocksByProductId(productId);
        assertEquals(1, result.size());
        assertEquals(productId, result.getFirst().getProductId());
        verify(stockRepository, times(1)).findAllByProductIdOrderByExpiryDateAsc(productId);
    }

    @Test
    public void findAllStocksByExpiryDateBefore_ShouldReturnStocks() {
        LocalDate expiryDate = LocalDate.now();
        List<Stock> mockStocks = List.of(new Stock(UUID.randomUUID(), 10, BigDecimal.valueOf(100),
                BigDecimal.valueOf(1000), UUID.randomUUID(), "prod123", LocalDate.now(), expiryDate));
        when(stockRepository.findAllByExpiryDateBefore(expiryDate)).thenReturn(
                List.of(new StockEntity(UUID.randomUUID(), 10, BigDecimal.valueOf(100),
                        BigDecimal.valueOf(1000), UUID.randomUUID(), "prod123", LocalDate.now(), expiryDate)));
        when(stockMapper.toDomain(any(StockEntity.class))).thenReturn(mockStocks.getFirst());
        List<Stock> result = stockPersistanceAdapter.findAllStocksByExpiryDateBefore(expiryDate);
        assertEquals(1, result.size());
        assertEquals(expiryDate, result.getFirst().getExpiryDate());
        verify(stockRepository, times(1)).findAllByExpiryDateBefore(expiryDate);
    }

    @Test
    public void readById_ShouldReturnStock() {
        UUID stockId = UUID.randomUUID();
        Stock mockStock = new Stock(stockId, 10, BigDecimal.valueOf(100),
                BigDecimal.valueOf(1000), UUID.randomUUID(), "prod123", LocalDate.now(), LocalDate.now());
        StockEntity stockEntity = new StockEntity(stockId, 10,
                BigDecimal.valueOf(100), BigDecimal.valueOf(1000), UUID.randomUUID(), "prod123",
                LocalDate.now(), LocalDate.now());

        when(stockRepository.findById(stockId)).thenReturn(java.util.Optional.of(stockEntity));
        when(stockMapper.toDomain(stockEntity)).thenReturn(mockStock);
        Stock result = stockPersistanceAdapter.readById(stockId);
        assertNotNull(result);
        assertEquals(stockId, result.getId());
        verify(stockRepository, times(1)).findById(stockId);
    }

    @Test
    public void create_ShouldReturnCreatedStock() {
        Stock mockStock = new Stock(UUID.randomUUID(), 10, BigDecimal.valueOf(100), BigDecimal.valueOf(1000),
                UUID.randomUUID(), "prod123", LocalDate.now(), LocalDate.now());
        StockEntity stockEntity = new StockEntity(UUID.randomUUID(), 10, BigDecimal.valueOf(100),
                BigDecimal.valueOf(1000), UUID.randomUUID(), "prod123", LocalDate.now(), LocalDate.now());

        when(stockMapper.toEntity(mockStock)).thenReturn(stockEntity);
        when(stockRepository.save(stockEntity)).thenReturn(stockEntity);
        when(stockMapper.toDomain(stockEntity)).thenReturn(mockStock);
        Stock result = stockPersistanceAdapter.create(mockStock);
        assertNotNull(result);
        assertEquals(mockStock.getProductId(), result.getProductId());
        verify(stockRepository, times(1)).save(stockEntity);
    }

    @Test
    public void update_ShouldReturnUpdatedStock() {
        UUID stockId = UUID.randomUUID();
        Stock mockStock = new Stock(stockId, 20, BigDecimal.valueOf(150), BigDecimal.valueOf(3000),
                UUID.randomUUID(), "prod123", LocalDate.now(), LocalDate.now());
        StockEntity existingStockEntity = new StockEntity(stockId, 10, BigDecimal.valueOf(100),
                BigDecimal.valueOf(1000), UUID.randomUUID(), "prod123", LocalDate.now(), LocalDate.now());

        when(stockRepository.findById(stockId)).thenReturn(java.util.Optional.of(existingStockEntity));
        when(stockMapper.toEntity(mockStock)).thenReturn(new StockEntity(stockId, 20, BigDecimal.valueOf(150),
                BigDecimal.valueOf(3000), UUID.randomUUID(), "prod123", LocalDate.now(), LocalDate.now()));
        when(stockRepository.save(existingStockEntity)).thenReturn(existingStockEntity);
        when(stockMapper.toDomain(existingStockEntity)).thenReturn(mockStock);
        Stock result = stockPersistanceAdapter.update(mockStock, stockId);
        assertNotNull(result);
        assertEquals(mockStock.getQuantity(), result.getQuantity());
        verify(stockRepository, times(1)).save(existingStockEntity);
    }

    @Test
    public void deleteById_ShouldDeleteStock() {
        UUID stockId = UUID.randomUUID();
        StockEntity stockEntity = new StockEntity(stockId, 10, BigDecimal.valueOf(100), BigDecimal.valueOf(1000),
                UUID.randomUUID(), "prod123", LocalDate.now(), LocalDate.now());
        when(stockRepository.findById(stockId)).thenReturn(java.util.Optional.of(stockEntity));
        stockPersistanceAdapter.deleteById(stockId);
        verify(stockRepository, times(1)).deleteById(stockId);
    }

    @Test
    public void readById_ShouldThrowStockNotFoundException_WhenStockNotFound() {
        UUID stockId = UUID.randomUUID();
        when(stockRepository.findById(stockId)).thenReturn(java.util.Optional.empty());
        assertThrows(StockNotFoundException.class, () -> stockPersistanceAdapter.readById(stockId));
    }
}
