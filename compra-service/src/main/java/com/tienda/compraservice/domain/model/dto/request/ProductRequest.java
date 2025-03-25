package com.tienda.compraservice.domain.model.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductRequest {

    @NotBlank(message = "El nombre del producto no puede estar vacio")
    @Size(max = 50, message = "El nombre del producto no puede tener más de 50 caracteres")
    String name;

    @Size(max = 255, message = "La descripción no puede tener mas de 255 caracteres")
    String description;

    @NotNull(message = "El precio de venta es obligatorio")
    @Positive(message = "El precio de venta debe ser un valor positivo")
    BigDecimal salePrice;

    @Size(max = 50, message = "La marca no puede tener mas de 50 caracteres")
    String brand;

    @NotBlank(message = "La categoría es obligatoria.")
    @Size(max = 50, message = "La categoria no puede tener mas de 50 caracteres")
    String category;
}
