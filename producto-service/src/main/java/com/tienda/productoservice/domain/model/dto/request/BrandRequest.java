package com.tienda.productoservice.domain.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BrandRequest {

    @NotBlank(message = "El nombre de la marca no puede estar vacio")
    @Size(max = 50, message = "El nombre de la marca no puede tener más de 50 caracteres")
    String name;

    @Size(max = 255, message = "La descripcion no puede tener mas de 255 caracteres")
    String description;
}
