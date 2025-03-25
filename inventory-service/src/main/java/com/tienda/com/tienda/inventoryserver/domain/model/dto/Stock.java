package com.tienda.com.tienda.inventoryserver.domain.model.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Stock {
    UUID id;
    Integer quantity;
    BigDecimal purchaseUnitCost;
    BigDecimal totalPurchaseCost;
    UUID providerId;
    String productId;
    LocalDate purchaseDate;
    LocalDate expiryDate;
}
