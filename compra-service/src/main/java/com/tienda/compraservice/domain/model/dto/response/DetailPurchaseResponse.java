package com.tienda.compraservice.domain.model.dto.response;

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
public class DetailPurchaseResponse {
    UUID id;
    String productId;
    Integer quantity;
    BigDecimal unitPrice;
    LocalDate expirationDate;
}
