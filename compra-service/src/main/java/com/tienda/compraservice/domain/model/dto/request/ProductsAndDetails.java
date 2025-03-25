package com.tienda.compraservice.domain.model.dto.request;

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
public class ProductsAndDetails {
    @NotNull(message = "El producto no puede ser nulo")
    ProductRequest product;

    @NotNull(message = "La cantidad no puede ser nula")
    @Positive(message = "La cantidad debe ser positiva")
    Integer quantity;

    @NotNull(message = "El precio unitario no puede ser nulo")
    @Positive(message = "El precio unitario no puede ser nevativo")
    BigDecimal unitPrice;

    LocalDate expirationDate;
}
