package com.tienda.compraservice.domain.model.dto;

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
public class DetailPurchase {
    UUID id;
    String productId;
    Integer quantity;
    LocalDate expirationDate;
    BigDecimal unitPrice;
}
