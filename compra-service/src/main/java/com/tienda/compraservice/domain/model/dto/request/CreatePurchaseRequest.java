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
public class CreatePurchaseRequest {

    @NotNull(message = "El proveedor no puede ser nulo")
    String provider;

    @NotNull(message = "Los detalles de la compra no pueden estar vacios")
    @Valid
    List<DetailPurchaseRequest> items;
}
