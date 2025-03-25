package com.tienda.com.tienda.inventoryserver.application.services;

import com.tienda.com.tienda.inventoryserver.domain.model.constant.TypeMove;
import com.tienda.com.tienda.inventoryserver.domain.model.dto.Kardex;
import com.tienda.com.tienda.inventoryserver.domain.model.dto.response.ReportInventoryResponse;
import com.tienda.com.tienda.inventoryserver.domain.port.KardexPersistancePort;
import com.tienda.com.tienda.inventoryserver.application.useCases.ReportEarings;
import com.tienda.com.tienda.inventoryserver.infraestructure.adapters.client.ProductFeignClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class EarningsReport implements ReportEarings {

    private final KardexPersistancePort kardexPersistancePort;
    private final ProductFeignClient productFeignClient;


    /**
     * Retrieves earnings details between two dates, including sales, costs, and profit margins
     *
     * @param startDate Start date of the report
     * @param endDate End date of the report
     * @return ReportInventoryResponse with financial details
     */
    @Override
    public ReportInventoryResponse earningsBetweenDatesDetailsProducts(LocalDate startDate, LocalDate endDate) {
        List<Kardex> movements = getMovementsBetweenDates(startDate, endDate);

        BigDecimal totalEarnings = BigDecimal.ZERO;
        BigDecimal totalCosts = BigDecimal.ZERO;
        int totalProductsSold = 0;
        Map<String, ReportInventoryResponse.ProductSalesDetail> productSalesMap = new HashMap<>();
        Map<String, BigDecimal> earningsByCategory = new HashMap<>();

        for (Kardex movement : movements) {
            if (movement.getTypeMovement() == TypeMove.OUTCOME) {
                totalEarnings = totalEarnings.add(movement.getTotalPrice());
                totalProductsSold += movement.getQuantity();
                processSale(movement, productSalesMap, earningsByCategory);
            } else if (movement.getTypeMovement() == TypeMove.INCOME) {
                totalCosts = totalCosts.add(movement.getTotalPrice());
                processIncome(movement, productSalesMap);
            }
        }

        return buildResponse(startDate, endDate, totalEarnings, totalCosts, totalProductsSold, productSalesMap, earningsByCategory);
    }

    /**
     * Fetches all movements within a given date range
     *
     * @param startDate Start date
     * @param endDate End date
     * @return List of Kardex movements
     */
    private List<Kardex> getMovementsBetweenDates(LocalDate startDate, LocalDate endDate) {
        return kardexPersistancePort.findAllKardexByMovementDateBetween(startDate, endDate);
    }

    /**
     * Processes a sale movement and updates product sales details
     *
     * @param movement Kardex movement
     * @param productSalesMap Map containing product sales details
     * @param earningsByCategory Map tracking earnings by product category
     */
    private void processSale(Kardex movement, Map<String,
            ReportInventoryResponse.ProductSalesDetail> productSalesMap, Map<String, BigDecimal> earningsByCategory) {
        var product = productFeignClient.getProductByCod(movement.getProductId());
        String productId = movement.getProductId();

        ReportInventoryResponse.ProductSalesDetail detail = productSalesMap.getOrDefault(productId,
                ReportInventoryResponse.ProductSalesDetail.builder()
                        .product(product)
                        .quantitySold(0)
                        .totalRevenue(BigDecimal.ZERO)
                        .profitMargin(BigDecimal.ZERO)
                        .build());

        detail.setQuantitySold(detail.getQuantitySold() + movement.getQuantity());
        detail.setTotalRevenue(detail.getTotalRevenue().add(movement.getTotalPrice()));
        productSalesMap.put(productId, detail);

        String category = product.getCategory().getName();
        BigDecimal categoryEarnings = earningsByCategory.getOrDefault(category, BigDecimal.ZERO);
        earningsByCategory.put(category, categoryEarnings.add(movement.getTotalPrice()));
    }

    /**
     * Processes an income movement and updates product cost details
     *
     * @param movement Kardex movement
     * @param productSalesMap Map containing product sales details
     */
    private void processIncome(Kardex movement, Map<String, ReportInventoryResponse.ProductSalesDetail> productSalesMap) {
        String productId = movement.getProductId();
        if (productSalesMap.containsKey(productId)) {
            var detail = productSalesMap.get(productId);
            BigDecimal margin = calculateProfitMargin(movement.getUnitPrice(), detail);
            detail.setProfitMargin(margin);
        }
    }

    /**
     * Calculates the profit margin for a product
     *
     * @param costPerUnit Cost per unit of the product
     * @param detail Product sales detail
     * @return Profit margin as a percentage
     */
    private BigDecimal calculateProfitMargin(BigDecimal costPerUnit, ReportInventoryResponse.ProductSalesDetail detail) {
        if (detail.getQuantitySold() == 0) return BigDecimal.ZERO;
        BigDecimal revenuePerUnit = detail.getTotalRevenue()
                .divide(new BigDecimal(detail.getQuantitySold()), 2, RoundingMode.HALF_UP);
        return revenuePerUnit.subtract(costPerUnit)
                .divide(revenuePerUnit, 4, RoundingMode.HALF_UP)
                .multiply(new BigDecimal("100"));
    }

    /**
     * Builds the final report response with calculated values
     *
     * @param startDate Start date
     * @param endDate End date
     * @param totalEarnings Total earnings
     * @param totalCosts Total costs
     * @param totalProductsSold Total products sold
     * @param productSalesMap Map containing product sales details
     * @param earningsByCategory Map tracking earnings by product category
     * @return ReportInventoryResponse with all financial details
     */
    private ReportInventoryResponse buildResponse(LocalDate startDate, LocalDate endDate,
                                                  BigDecimal totalEarnings, BigDecimal totalCosts,
                                                  int totalProductsSold,
                                                  Map<String, ReportInventoryResponse.ProductSalesDetail> productSalesMap,
                                                  Map<String, BigDecimal> earningsByCategory) {
        BigDecimal netProfit = totalEarnings.subtract(totalCosts);
        List<ReportInventoryResponse.ProductSalesDetail> topSellingProducts = productSalesMap.values().stream()
                .sorted(Comparator.comparing(ReportInventoryResponse.ProductSalesDetail::getTotalRevenue).reversed())
                .limit(10)
                .toList();

        return ReportInventoryResponse.builder()
                .totalEarnings(totalEarnings)
                .totalCosts(totalCosts)
                .netProfit(netProfit)
                .totalProductsSold(totalProductsSold)
                .topSellingProducts(topSellingProducts)
                .earningsByCategory(earningsByCategory)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }
}
