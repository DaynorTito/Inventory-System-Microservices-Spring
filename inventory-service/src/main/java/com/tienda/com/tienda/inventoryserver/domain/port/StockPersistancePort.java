package com.tienda.com.tienda.inventoryserver.domain.port;

import com.tienda.com.tienda.inventoryserver.domain.abstraction.PersistancePort;
import com.tienda.com.tienda.inventoryserver.domain.model.dto.Stock;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface StockPersistancePort extends PersistancePort<Stock, UUID>{
    List<Stock> findAllStocksByProductId(String productId);
    List<Stock> findAllStocksByExpiryDateBefore(LocalDate expiryDate);
    List<Stock> findAllStocksByPurchaseDateBetween(LocalDate purchaseDateAfter, LocalDate purchaseDateBefore);
    List<Stock> findAllStocksByQuantityLessThan(Integer quantity);
    List<Stock> findAllByProvider(UUID uuid);
    List<Stock> findAllStocks();
    Stock updateDecrement(Stock request, UUID uuid);
}
