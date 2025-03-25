package com.tienda.com.tienda.inventoryserver.application.services;

import com.tienda.com.tienda.inventoryserver.application.mapper.StockDomainMapper;
import com.tienda.com.tienda.inventoryserver.application.validation.FeignValidator;
import com.tienda.com.tienda.inventoryserver.domain.model.dto.Product;
import com.tienda.com.tienda.inventoryserver.domain.model.dto.Stock;
import com.tienda.com.tienda.inventoryserver.domain.model.dto.request.StockRequest;
import com.tienda.com.tienda.inventoryserver.domain.model.dto.response.StockResponse;
import com.tienda.com.tienda.inventoryserver.domain.port.StockPersistancePort;
import com.tienda.com.tienda.inventoryserver.infraestructure.adapters.client.ProductFeignClient;
import com.tienda.com.tienda.inventoryserver.infraestructure.adapters.exception.InsufficientStock;
import com.tienda.com.tienda.inventoryserver.infraestructure.adapters.exception.StockNotFoundException;
import com.tienda.com.tienda.inventoryserver.infraestructure.adapters.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StockServiceTest {
    @Mock
    private ProductFeignClient productFeignClient;

    @Mock
    private StockDomainMapper stockDomainMapper;

    @Mock
    private StockPersistancePort stockPersistancePort;

    @Mock
    private FeignValidator feignValidator;

    @InjectMocks
    @Spy
    private StockService stockService;

    private Stock stock;
    private Stock expiredStock;
    private StockResponse stockResponse;
    private StockRequest stockRequest;
    private Product product;
    private final UUID stockId = UUID.randomUUID();
    private final UUID providerId = UUID.randomUUID();
    private final String productId = "PROD001";

    @BeforeEach
    public void setUp() {
        stock = new Stock();
        stock.setId(stockId);
        stock.setProductId(productId);
        stock.setProviderId(providerId);
        stock.setQuantity(10);
        stock.setPurchaseUnitCost(BigDecimal.valueOf(100));
        stock.setTotalPurchaseCost(BigDecimal.valueOf(1000));
        stock.setPurchaseDate(LocalDate.now());
        stock.setExpiryDate(LocalDate.now().plusMonths(6));

        expiredStock = new Stock();
        expiredStock.setId(UUID.randomUUID());
        expiredStock.setProductId(productId);
        expiredStock.setProviderId(providerId);
        expiredStock.setQuantity(5);
        expiredStock.setPurchaseUnitCost(BigDecimal.valueOf(100));
        expiredStock.setTotalPurchaseCost(BigDecimal.valueOf(500));
        expiredStock.setPurchaseDate(LocalDate.now().minusMonths(8));
        expiredStock.setExpiryDate(LocalDate.now().minusDays(1));

        stockResponse = new StockResponse();
        stockResponse.setId(stockId);
        stockResponse.setQuantity(10);
        stockResponse.setPurchaseUnitCost(BigDecimal.valueOf(100));
        stockResponse.setTotalPurchaseCost(BigDecimal.valueOf(1000));
        stockResponse.setProviderId(providerId);

        product = new Product();
        product.setCod(productId);
        product.setName("Test Product");
        product.setCategory(new Product.Category("TestCategory", "Test Category Description"));
        product.setBrand(new Product.Brand("TestBrand", "Test Brand Description"));
        product.setSalePrice(BigDecimal.valueOf(150));

        stockResponse.setProductId(product);

        stockRequest = new StockRequest();
        stockRequest.setProductId(productId);
        stockRequest.setProviderId(providerId);
        stockRequest.setQuantity(10);
        stockRequest.setPurchaseUnitCost(BigDecimal.valueOf(100));
        stockRequest.setExpiryDate(LocalDate.now().plusMonths(6));
    }

    @Test
    public void getAllStocks_ShouldReturnStockList() {
        List<Stock> stocks = Collections.singletonList(stock);
        when(stockPersistancePort.findAllStocks()).thenReturn(stocks);
        when(stockDomainMapper.stockToStockResponse(any(Stock.class))).thenReturn(stockResponse);
        List<StockResponse> result = stockService.getAllStocks();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(stockResponse, result.getFirst());
        verify(stockPersistancePort).findAllStocks();
        verify(stockDomainMapper, times(1)).stockToStockResponse(any(Stock.class));
    }

    @Test
    public void getById_ShouldReturnStock() {
        when(stockPersistancePort.readById(stockId)).thenReturn(stock);
        when(stockDomainMapper.stockToStockResponse(stock)).thenReturn(stockResponse);
        StockResponse result = stockService.getById(stockId);
        assertNotNull(result);
        assertEquals(stockResponse, result);
        verify(stockPersistancePort).readById(stockId);
        verify(stockDomainMapper).stockToStockResponse(stock);
    }

    @Test
    public void createEntity_ShouldCreateAndReturnStock() {
        // Arrange
        when(stockDomainMapper.stockRequestToStock(stockRequest)).thenReturn(stock);
        when(stockPersistancePort.create(any(Stock.class))).thenReturn(stock);
        when(stockDomainMapper.stockToStockResponse(stock)).thenReturn(stockResponse);
        doNothing().when(feignValidator).verifyExistingProduct(productId);
        doNothing().when(feignValidator).verifyExistingProvider(providerId);
        StockResponse result = stockService.createEntity(stockRequest);
        assertNotNull(result);
        assertEquals(stockResponse, result);
        verify(stockDomainMapper).stockRequestToStock(stockRequest);
        verify(stockPersistancePort).create(any(Stock.class));
        verify(stockDomainMapper).stockToStockResponse(stock);
        verify(feignValidator).verifyExistingProduct(productId);
        verify(feignValidator).verifyExistingProvider(providerId);
    }

    @Test
    public void updateEntity_ShouldUpdateAndReturnStock() {
        when(stockDomainMapper.stockRequestToStock(stockRequest)).thenReturn(stock);
        when(stockPersistancePort.update(stock, stockId)).thenReturn(stock);
        when(stockDomainMapper.stockToStockResponse(stock)).thenReturn(stockResponse);
        StockResponse result = stockService.updateEntity(stockRequest, stockId);
        assertNotNull(result);
        assertEquals(stockResponse, result);
        verify(stockDomainMapper).stockRequestToStock(stockRequest);
        verify(stockPersistancePort).update(stock, stockId);
        verify(stockDomainMapper).stockToStockResponse(stock);
    }

    @Test
    public void deleteEntityById_ShouldDeleteStock() {
        doNothing().when(stockPersistancePort).deleteById(stockId);
        stockService.deleteEntityById(stockId);
        verify(stockPersistancePort).deleteById(stockId);
    }

    @Test
    public void updateQuantity_ShouldUpdateStockQuantity() {
        List<Stock> stocks = new ArrayList<>();
        stocks.add(stock);
        when(stockPersistancePort.findAllStocksByProductId(productId)).thenReturn(stocks);
        when(stockPersistancePort.update(any(Stock.class), any(UUID.class))).thenReturn(stock);
        when(stockDomainMapper.stockToStockResponse(stock)).thenReturn(stockResponse);
        StockResponse result = stockService.updateQuantity(productId, 15);
        assertNotNull(result);
        assertEquals(stockResponse, result);
        verify(stockPersistancePort).findAllStocksByProductId(productId);
        verify(stockPersistancePort).update(any(Stock.class), any(UUID.class));
        verify(stockDomainMapper).stockToStockResponse(stock);
    }

    @Test
    public void incrementQuantity_ShouldIncrementStockQuantity() {
        List<Stock> stocks = new ArrayList<>();
        stocks.add(stock);
        when(stockPersistancePort.findAllStocksByProductId(productId)).thenReturn(stocks);
        when(stockPersistancePort.update(any(Stock.class), any(UUID.class))).thenReturn(stock);
        when(stockDomainMapper.stockToStockResponse(stock)).thenReturn(stockResponse);
        StockResponse result = stockService.incrementQuantity(productId, 5);
        assertNotNull(result);
        assertEquals(stockResponse, result);
        verify(stockPersistancePort).findAllStocksByProductId(productId);
        verify(stockPersistancePort).update(any(Stock.class), any(UUID.class));
        verify(stockDomainMapper).stockToStockResponse(stock);
    }

    @Test
    public void decrementQuantity_ShouldDecrementStockQuantity() {
        List<Stock> stocks = new ArrayList<>();
        stocks.add(stock);
        when(stockPersistancePort.findAllStocksByProductId(productId)).thenReturn(stocks);
        when(stockPersistancePort.updateDecrement(any(Stock.class), any(UUID.class))).thenReturn(stock);
        when(stockDomainMapper.stockToStockResponse(stock)).thenReturn(stockResponse);
        doReturn(10).when(stockService).getStockWithoutExpiringDate(productId);
        doReturn(Map.of("Stock expirado", 0)).when(stockService).getExpiredStock(productId);
        StockResponse result = stockService.decrementQuantity(productId, 5, BigDecimal.valueOf(120));
        assertNotNull(result);
        assertEquals(stockResponse, result);
        verify(stockPersistancePort).findAllStocksByProductId(productId);
        verify(stockPersistancePort).updateDecrement(any(Stock.class), any(UUID.class));
        verify(stockDomainMapper).stockToStockResponse(stock);
    }

    @Test
    public void decrementQuantity_ShouldThrowInsufficientStockException_WhenInsufficientStock() {
        List<Stock> stocks = new ArrayList<>();
        stocks.add(stock);
        when(stockPersistancePort.findAllStocksByProductId(productId)).thenReturn(stocks);
        doReturn(5).when(stockService).getStockWithoutExpiringDate(productId);
        doReturn(Map.of("Stock expirado", 0)).when(stockService).getExpiredStock(productId);

        assertThrows(InsufficientStock.class, () ->
                stockService.decrementQuantity(productId, 10, BigDecimal.valueOf(120))
        );
        verify(stockPersistancePort).findAllStocksByProductId(productId);
    }

    @Test
    public void decrementQuantity_ShouldThrowValidationException_WhenInvalidPriceRange() {
        List<Stock> stocks = new ArrayList<>();
        stocks.add(stock);
        when(stockPersistancePort.findAllStocksByProductId(productId)).thenReturn(stocks);
        doReturn(10).when(stockService).getStockWithoutExpiringDate(productId);
        doReturn(Map.of("Stock expirado", 0)).when(stockService).getExpiredStock(productId);
        assertThrows(ValidationException.class, () ->
                stockService.decrementQuantity(productId, 5, BigDecimal.valueOf(50))
        );
        verify(stockPersistancePort).findAllStocksByProductId(productId);
    }

    @Test
    public void getTotalStock_ShouldReturnStockInfo() {
        List<Stock> stocks = new ArrayList<>();
        stocks.add(stock);
        when(stockPersistancePort.findAllStocksByProductId(productId)).thenReturn(stocks);
        when(productFeignClient.getProductByCod(productId)).thenReturn(product);
        doReturn(10).when(stockService).getStockWithoutExpiringDate(productId);
        doReturn(Map.of("Stock expirado", 0)).when(stockService).getExpiredStock(productId);
        Map<String, String> result = stockService.getTotalStock(productId);
        assertNotNull(result);
        assertEquals("Test Product", result.get("Nombre de producto"));
        assertEquals("10", result.get("Stock valido: "));
        assertEquals("0", result.get("Stock caducado: "));
        assertEquals("10", result.get("Total: "));
        verify(stockPersistancePort).findAllStocksByProductId(productId);
        verify(productFeignClient).getProductByCod(productId);
    }

    @Test
    public void getOldestStock_ShouldReturnOldestStock() {
        List<Stock> stocks = new ArrayList<>();
        stocks.add(stock);
        when(stockPersistancePort.findAllStocksByProductId(productId)).thenReturn(stocks);
        when(stockDomainMapper.stockToStockResponse(stock)).thenReturn(stockResponse);
        StockResponse result = stockService.getOldestStock(productId);
        assertNotNull(result);
        assertEquals(stockResponse, result);
        verify(stockPersistancePort).findAllStocksByProductId(productId);
        verify(stockDomainMapper).stockToStockResponse(stock);
    }

    @Test
    public void getExpiringDate_ShouldReturnExpiringStocks() {
        List<Stock> stocks = Collections.singletonList(stock);
        when(stockPersistancePort.findAllStocksByExpiryDateBefore(any(LocalDate.class))).thenReturn(stocks);
        when(stockDomainMapper.stockToStockResponse(any(Stock.class))).thenReturn(stockResponse);
        List<StockResponse> result = stockService.getExpiringDate(30);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(stockResponse, result.getFirst());
        verify(stockPersistancePort).findAllStocksByExpiryDateBefore(any(LocalDate.class));
        verify(stockDomainMapper, times(1)).stockToStockResponse(any(Stock.class));
    }

    @Test
    public void checkStockThreshold_ShouldReturnProductsBelowThreshold() {
        List<Stock> stocks = Collections.singletonList(stock);
        when(stockPersistancePort.findAllStocks()).thenReturn(stocks);
        when(productFeignClient.getProductByCod(productId)).thenReturn(product);
        List<Product> result = stockService.checkStockThreshold(15, null);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(product, result.getFirst());
        verify(stockPersistancePort).findAllStocks();
        verify(productFeignClient).getProductByCod(productId);
    }

    @Test
    public void getStocksByProviderId_ShouldReturnStocksForProvider() {
        List<Stock> stocks = Collections.singletonList(stock);
        when(stockPersistancePort.findAllByProvider(providerId)).thenReturn(stocks);
        when(stockDomainMapper.stockToStockResponse(any(Stock.class))).thenReturn(stockResponse);
        List<StockResponse> result = stockService.getStocksByProviderId(providerId);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(stockResponse, result.getFirst());
        verify(stockPersistancePort).findAllByProvider(providerId);
        verify(stockDomainMapper, times(1)).stockToStockResponse(any(Stock.class));
    }

    @Test
    public void getExpiredStock_ShouldReturnExpiredStockCount() {
        List<Stock> stocks = Collections.singletonList(expiredStock);
        when(stockPersistancePort.findAllStocksByProductId(productId)).thenReturn(stocks);
        Map<String, Integer> result = stockService.getExpiredStock(productId);
        assertNotNull(result);
        assertEquals(5, result.get("Stock expirado"));
        verify(stockPersistancePort).findAllStocksByProductId(productId);
    }

    @Test
    public void getStockWithoutExpiringDate_ShouldReturnValidStockCount() {
        List<Stock> stocks = Collections.singletonList(stock);
        when(stockPersistancePort.findAllStocksByProductId(productId)).thenReturn(stocks);
        Integer result = stockService.getStockWithoutExpiringDate(productId);
        assertNotNull(result);
        assertEquals(10, result);
        verify(stockPersistancePort).findAllStocksByProductId(productId);
    }

    @Test
    public void getAllStocksBetweenDates_ShouldReturnStocksInDateRange() {
        List<Stock> stocks = Collections.singletonList(stock);
        LocalDate startDate = LocalDate.now().minusDays(30);
        LocalDate endDate = LocalDate.now();
        when(stockPersistancePort.findAllStocksByPurchaseDateBetween(startDate, endDate)).thenReturn(stocks);
        when(stockDomainMapper.stockToStockResponse(any(Stock.class))).thenReturn(stockResponse);
        List<StockResponse> result = stockService.getAllStocksBetweenDates(startDate, endDate);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(stockResponse, result.getFirst());
        verify(stockPersistancePort).findAllStocksByPurchaseDateBetween(startDate, endDate);
        verify(stockDomainMapper, times(1)).stockToStockResponse(any(Stock.class));
    }


    @Test
    public void validateExistingStock_ShouldThrowException_WhenStockNotFound() {
        when(stockPersistancePort.findAllStocksByProductId(productId)).thenReturn(Collections.emptyList());
        assertThrows(StockNotFoundException.class, () ->
                stockService.validateExistingStock(productId)
        );
        verify(stockPersistancePort).findAllStocksByProductId(productId);
    }
}
