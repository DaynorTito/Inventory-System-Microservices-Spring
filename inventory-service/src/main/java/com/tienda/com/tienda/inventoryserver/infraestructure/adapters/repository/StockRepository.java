package com.tienda.com.tienda.inventoryserver.infraestructure.adapters.repository;

import com.tienda.com.tienda.inventoryserver.infraestructure.adapters.entity.StockEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface StockRepository extends JpaRepository<StockEntity, UUID> {
    List<StockEntity> findAllByProductIdOrderByExpiryDateAsc(String productId);
    List<StockEntity> findAllByExpiryDateBefore(LocalDate expiryDate);
    List<StockEntity> findAllByPurchaseDateBetween(LocalDate purchaseDateAfter, LocalDate purchaseDateBefore);
    List<StockEntity> findAllByQuantityLessThan(Integer quantity);
    List<StockEntity> findAllByProviderIdOrderByExpiryDateAsc(UUID providerId);
}
