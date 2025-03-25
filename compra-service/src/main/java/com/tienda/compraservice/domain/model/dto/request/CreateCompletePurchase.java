package com.tienda.compraservice.domain.model.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateCompletePurchase {

    @NotNull(message = "El proveedor no puede ser nulo")
    String provider;

    @Valid
    @NotNull(message = "Los detalles de los productos no pueden estar vacios")
    List<ProductsAndDetails> items;
}
