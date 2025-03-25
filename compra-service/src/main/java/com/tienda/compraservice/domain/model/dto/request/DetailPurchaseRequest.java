package com.tienda.compraservice.domain.model.dto.request;

import jakarta.validation.constraints.Future;
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
public class DetailPurchaseRequest {

    @NotNull(message = "El codigo de producto no puede ser nulo")
    String productId;

    @NotNull(message = "La cantidad no puede ser nula")
    @Positive(message = "La cantidad debe ser positiva")
    Integer quantity;

    @NotNull(message = "El precio unitario no puede ser nulo")
    @Positive(message = "La cantidad debe ser positiva")
    BigDecimal unitPrice;

    @Future(message = "La fecha de expiracion debe ser futura")
    LocalDate expirationDate;
}
