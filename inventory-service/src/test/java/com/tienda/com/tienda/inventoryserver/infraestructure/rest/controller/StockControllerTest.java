package com.tienda.com.tienda.inventoryserver.infraestructure.rest.controller;


import com.tienda.com.tienda.inventoryserver.application.services.StockService;
import com.tienda.com.tienda.inventoryserver.domain.model.constant.StockStatus;
import com.tienda.com.tienda.inventoryserver.domain.model.dto.Product;
import com.tienda.com.tienda.inventoryserver.domain.model.dto.request.StockRequest;
import com.tienda.com.tienda.inventoryserver.domain.model.dto.response.StockResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockControllerTest {

    @Mock
    private StockService stockService;

    @InjectMocks
    private StockController stockController;

    private UUID stockId;
    private StockResponse stockResponse;
    private StockRequest stockRequest;

    @BeforeEach
    void setUp() {
        stockId = UUID.randomUUID();
        stockResponse = new StockResponse();
        stockRequest = new StockRequest();
    }

    @Test
    void getAllStocks_ShouldReturnListOfStocks() {
        when(stockService.getAllStocks()).thenReturn(List.of(stockResponse));
        ResponseEntity<List<StockResponse>> response = stockController.getAllStocks();
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getStockById_ShouldReturnStockResponse() {
        when(stockService.getById(stockId)).thenReturn(stockResponse);
        ResponseEntity<StockResponse> response = stockController.getStockById(stockId);
        assertEquals(stockResponse, response.getBody());
    }

    @Test
    void createStock_ShouldReturnCreatedStock() {
        when(stockService.createEntity(stockRequest)).thenReturn(stockResponse);
        ResponseEntity<StockResponse> response = stockController.createStock(stockRequest);
        assertEquals(stockResponse, response.getBody());
        assertEquals(201, response.getStatusCodeValue());
    }

    @Test
    void updateStock_ShouldReturnUpdatedStock() {
        when(stockService.updateEntity(stockRequest, stockId)).thenReturn(stockResponse);
        ResponseEntity<StockResponse> response = stockController.updateStock(stockId, stockRequest);
        assertEquals(stockResponse, response.getBody());
    }

    @Test
    void deleteStock_ShouldReturnNoContent() {
        doNothing().when(stockService).deleteEntityById(stockId);
        ResponseEntity<Void> response = stockController.deleteStock(stockId);
        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    void getTotalStock_ShouldReturnStockTotal() {
        String productId = "123";
        Map<String, String> totalStock = Collections.singletonMap("total", "100");
        when(stockService.getTotalStock(productId)).thenReturn(totalStock);
        ResponseEntity<Map<String, String>> response = stockController.getTotalStock(productId);
        assertEquals(totalStock, response.getBody());
    }

    @Test
    void incrementStock_ShouldReturnUpdatedStock() {
        String productId = "123";
        int quantity = 10;
        when(stockService.incrementQuantity(productId, quantity)).thenReturn(stockResponse);
        ResponseEntity<StockResponse> response = stockController.incrementStock(productId, quantity);
        assertEquals(stockResponse, response.getBody());
    }

    @Test
    void getOldestStock_ShouldReturnOldestStock() {
        String productId = "123";
        when(stockService.getOldestStock(productId)).thenReturn(stockResponse);
        ResponseEntity<StockResponse> response = stockController.getOldestStock(productId);
        assertEquals(stockResponse, response.getBody());
    }

    @Test
    void getExpiringStock_ShouldReturnExpiringStockList() {
        int days = 5;
        when(stockService.getExpiringDate(days)).thenReturn(List.of(stockResponse));
        ResponseEntity<List<StockResponse>> response = stockController.getExpiringStock(days);
        assertEquals(1, Objects.requireNonNull(response.getBody()).size());
    }

    @Test
    void checkStockThreshold_ShouldReturnProductList() {
        int threshold = 50;
        String category = "Electronics";
        when(stockService.checkStockThreshold(threshold, category)).thenReturn(List.of(new Product()));
        ResponseEntity<List<Product>> response = stockController.checkStockThreshold(threshold, category);
        assertEquals(1, Objects.requireNonNull(response.getBody()).size());
    }

    @Test
    void getStocksByProvider_ShouldReturnStockList() {
        UUID providerId = UUID.randomUUID();
        when(stockService.getStocksByProviderId(providerId)).thenReturn(List.of(stockResponse));
        ResponseEntity<List<StockResponse>> response = stockController.getStocksByProvider(providerId);
        assertEquals(1, Objects.requireNonNull(response.getBody()).size());
    }

    @Test
    void getStocksBetweenDates_ShouldReturnStockList() {
        LocalDate startDate = LocalDate.now().minusDays(10);
        LocalDate endDate = LocalDate.now();
        when(stockService.getAllStocksBetweenDates(startDate, endDate)).thenReturn(List.of(stockResponse));
        ResponseEntity<List<StockResponse>> response = stockController.getStocksBetweenDates(startDate, endDate);
        assertEquals(1, Objects.requireNonNull(response.getBody()).size());
    }

    @Test
    void getInventoryStatus_ShouldReturnStockStatusMap() {
        Map<String, Integer> thresholds = Collections.singletonMap("product1", 10);
        Map<String, StockStatus> statusMap = Collections.singletonMap("product1", StockStatus.LOW_STOCK);
        when(stockService.getInventoryStatus(thresholds)).thenReturn(statusMap);
        ResponseEntity<Map<String, StockStatus>> response = stockController.getInventoryStatus(thresholds);
        assertEquals(statusMap, response.getBody());
    }
}