package com.tienda.com.tienda.inventoryserver.application.useCases;

import com.tienda.com.tienda.inventoryserver.domain.abstraction.CrudService;
import com.tienda.com.tienda.inventoryserver.domain.model.constant.StockStatus;
import com.tienda.com.tienda.inventoryserver.domain.model.dto.Product;
import com.tienda.com.tienda.inventoryserver.domain.model.dto.Stock;
import com.tienda.com.tienda.inventoryserver.domain.model.dto.request.StockRequest;
import com.tienda.com.tienda.inventoryserver.domain.model.dto.response.StockResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface StockUseCases extends CrudService<StockRequest, StockResponse, UUID> {
    StockResponse updateQuantity(String productId, int newQuantity);
    StockResponse incrementQuantity(String productId, int quantity);
    StockResponse decrementQuantity(String productId, int quantity, BigDecimal decrement);
    Map<String, String> getTotalStock(String productId);
    StockResponse getOldestStock(String productId);
    List<StockResponse> getAllStocks();
    List<StockResponse> getExpiringDate(Integer days);
    List<Product> checkStockThreshold(Integer threshold, String category);
    List<StockResponse> getStocksByProviderId(UUID providerId);
    Map<String, Integer> getExpiredStock(String productId);
    Integer getStockWithoutExpiringDate(String productId);
    List<StockResponse> getAllStocksBetweenDates(LocalDate startDate, LocalDate endDate);
    Map<String, StockStatus> getInventoryStatus(Map<String, Integer> lowStockThresholds);
}
