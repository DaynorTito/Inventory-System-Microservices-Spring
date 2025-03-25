package com.tienda.com.tienda.inventoryserver.domain.model.dto.response;


import com.tienda.com.tienda.inventoryserver.domain.model.dto.Product;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReportInventoryResponse {
    BigDecimal totalEarnings;
    BigDecimal totalCosts;
    BigDecimal netProfit;
    Integer totalProductsSold;
    List<ProductSalesDetail> topSellingProducts;
    Map<String, BigDecimal> earningsByCategory;
    LocalDate startDate;
    LocalDate endDate;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProductSalesDetail {
        Product product;
        Integer quantitySold;
        BigDecimal totalRevenue;
        BigDecimal profitMargin;
    }
}
