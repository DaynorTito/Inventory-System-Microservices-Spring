package com.tienda.com.tienda.inventoryserver.domain.model.dto.request;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SaleInventoryRequest {
    Integer quantity;
    String productId;
    BigDecimal unitPrice;
}
