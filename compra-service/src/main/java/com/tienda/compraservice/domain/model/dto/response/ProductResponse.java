package com.tienda.compraservice.domain.model.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse {
    String cod;
    String name;
    String description;
    Category category;
    Brand brand;
    BigDecimal salePrice;
    LocalDateTime creationDate;
}

@Data
class Category {
    Long id;
    String name;
    String description;
}

@Data
class Brand {
    Long id;
    String name;
    String description;
}
