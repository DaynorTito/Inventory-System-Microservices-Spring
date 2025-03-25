package com.tienda.com.tienda.inventoryserver.domain.model.dto.request;


import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
public class StockRequest {
    @NotNull(message = "La cantidad no puede ser nula")
    @Positive(message = "La cantidad debe ser positiva")
    Integer quantity;

    @NotNull(message = "El costo unitario de compra no puede ser nulo")
    @Positive(message = "El costo unitario debe ser positivo")
    BigDecimal purchaseUnitCost;

    @NotNull(message = "El ID del proveedor no puede ser nulo")
    UUID providerId;

    @NotNull(message = "El ID del producto no puede ser nulo")
    String productId;

    @NotNull(message = "La fecha de compra no puede ser nula")
    LocalDate purchaseDate;

    @Future(message = "La fecha de expiración debe ser una fecha futura")
    LocalDate expiryDate;
}
