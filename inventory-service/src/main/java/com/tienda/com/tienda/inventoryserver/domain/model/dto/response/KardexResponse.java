package com.tienda.com.tienda.inventoryserver.domain.model.dto.response;


import com.tienda.com.tienda.inventoryserver.domain.model.constant.TypeMove;
import com.tienda.com.tienda.inventoryserver.domain.model.dto.Product;
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
public class KardexResponse {
    UUID id;
    TypeMove typeMovement;
    Integer quantity;
    Product productId;
    BigDecimal unitPrice;
    BigDecimal totalPrice;
    LocalDate movementDate;
}
