package com.tienda.salieservice.infrastructure.adapters.repository;

import com.tienda.salieservice.infrastructure.adapters.entity.SaleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface SaleRepository extends JpaRepository<SaleEntity, UUID> {
    List<SaleEntity> findBySaleDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<SaleEntity> findBySaleDetails_ProductId(String productId);
    List<SaleEntity> findBySaleDetails_ProductIdAndSaleDateBetween(
            String productId, LocalDateTime startDate, LocalDateTime endDate);
    List<SaleEntity> findByCustomerNameContainingIgnoreCase(String customerName);
    List<SaleEntity> findByCustomerNameContainingIgnoreCaseAndSaleDateBetween(
            String customerName, LocalDateTime startDate, LocalDateTime endDateTime);
    List<SaleEntity> findByTotalAmountGreaterThanEqual(BigDecimal price);
    List<SaleEntity> findByTotalAmountLessThanEqual(BigDecimal price);
}
