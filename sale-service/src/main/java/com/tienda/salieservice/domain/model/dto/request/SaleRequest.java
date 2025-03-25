package com.tienda.salieservice.domain.model.dto.request;


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
public class SaleRequest {

    @NotNull(message = "Debe ingresar el nombre del cliente")
    String customerName;

    @NotNull(message = "Debe ingresar el metodo de pago")
    String paymentMethod;

    @NotNull(message = "La lista de detalles no debe estar vacia")
    @Valid
    List<SaleDetailsRequest> saleDetails;
}
