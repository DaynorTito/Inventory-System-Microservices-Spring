package com.tienda.com.tienda.inventoryserver.domain.model.dto;

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
