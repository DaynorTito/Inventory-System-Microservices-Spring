package com.tienda.com.tienda.inventoryserver.domain.model.dto.request;


import com.tienda.com.tienda.inventoryserver.domain.model.constant.TypeMove;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KardexRequest {
    @NotNull(message = "El tipo de movimiento no puede ser nulo")
    TypeMove typeMovement;

    @NotNull(message = "La cantidad no puede ser nula")
    @Positive(message = "La cantidad debe ser positiva")
    Integer quantity;

    @NotNull(message = "El ID del producto no puede ser nulo")
    String productId;

    @NotNull(message = "El precio unitario no puede ser nulo")
    @Positive(message = "El precio unitario debe ser positivo")
    BigDecimal unitPrice;

    LocalDate movementDate;
}
