package com.tienda.productoservice.domain.model.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product {
    String cod;
    String name;
    String description;
    Category category;
    Brand brand;
    BigDecimal discount;
    BigDecimal salePrice;
    LocalDateTime creationDate;
}
