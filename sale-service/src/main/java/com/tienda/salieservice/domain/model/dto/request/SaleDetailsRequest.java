package com.tienda.salieservice.domain.model.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SaleDetailsRequest {

    @NotNull(message = "El identificador del producto no debe estar vacio")
    String productId;

    @NotNull(message = "La cantidad producto no debe estar vacio")
    @Positive(message = "La cantidad debe ser positiva")
    Integer quantity;

    @Positive(message = "El precio no debe ser negativo")
    BigDecimal unitPrice;

    @Positive(message = "El precio de descuento no debe ser negativo")
    BigDecimal discount;
}
