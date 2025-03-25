package com.tienda.salieservice.domain.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
public class Product {
    String cod;
    String name;
    Category category;
    Brand brand;
    BigDecimal salePrice;
    BigDecimal discount;


    @Data
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Category {
        String name;
        String description;
    }

    @Data
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Brand {
        String name;
        String description;
    }
}
