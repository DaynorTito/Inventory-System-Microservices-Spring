package com.tienda.com.tienda.inventoryserver.infraestructure.adapters.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity(name = "stock")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class StockEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private Integer quantity;

    @Column(nullable = false, name = "purchase_unit_cost")
    private BigDecimal purchaseUnitCost;

    @Column(nullable = false, name = "total_purchase_cost")
    private BigDecimal totalPurchaseCost;

    @Column(nullable = false, name = "provider_id")
    private UUID providerId;

    @Column(nullable = false, name = "product_id")
    private String productId;

    @Column(nullable = false, name = "purchase_date")
    private LocalDate purchaseDate;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;
}
