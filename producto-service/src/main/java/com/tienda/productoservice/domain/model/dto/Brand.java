package com.tienda.productoservice.domain.model.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Brand {
    Long id;
    String name;
    String description;
}
