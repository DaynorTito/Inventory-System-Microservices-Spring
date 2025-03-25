package com.tienda.com.tienda.inventoryserver.domain.model.dto;

import com.tienda.com.tienda.inventoryserver.domain.model.constant.TypeMove;
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
public class Kardex {
    UUID id;
    TypeMove typeMovement;
    Integer quantity;
    String productId;
    BigDecimal unitPrice;
    BigDecimal totalPrice;
    LocalDate movementDate;
}
