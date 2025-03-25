package com.tienda.com.tienda.inventoryserver.application.services;

import com.tienda.com.tienda.inventoryserver.domain.model.constant.TypeMove;
import com.tienda.com.tienda.inventoryserver.domain.model.dto.Kardex;
import com.tienda.com.tienda.inventoryserver.domain.model.dto.Product;
import com.tienda.com.tienda.inventoryserver.domain.model.dto.response.ReportInventoryResponse;
import com.tienda.com.tienda.inventoryserver.domain.port.KardexPersistancePort;
import com.tienda.com.tienda.inventoryserver.infraestructure.adapters.client.ProductFeignClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EarningsReportTest {

    @Mock
    private KardexPersistancePort kardexPersistancePort;

    @Mock
    private ProductFeignClient productFeignClient;

    @InjectMocks
    private EarningsReport earningsReport;

    private LocalDate startDate;
    private LocalDate endDate;
    private Product product1;
    private Product product2;
    private Product.Category category1;
    private Kardex saleMoveProduct1;
    private Kardex purchaseMoveProduct1;
    private Kardex saleMoveProduct2;

    @BeforeEach
    void setUp() {
        startDate = LocalDate.of(2025, 1, 1);
        endDate = LocalDate.of(2025, 1, 31);

        category1 = new Product.Category();
        category1.setName("Electronics");

        Product.Category category2 = new Product.Category();
        category2.setName("Clothing");

        product1 = new Product();
        product1.setCod("PROD-001");
        product1.setName("Laptop");
        product1.setCategory(category1);

        product2 = new Product();
        product2.setCod("PROD-002");
        product2.setName("T-Shirt");
        product2.setCategory(category2);

        saleMoveProduct1 = new Kardex();
        saleMoveProduct1.setId(java.util.UUID.randomUUID());
        saleMoveProduct1.setProductId("PROD-001");
        saleMoveProduct1.setTypeMovement(TypeMove.OUTCOME);
        saleMoveProduct1.setQuantity(2);
        saleMoveProduct1.setUnitPrice(new BigDecimal("1000.00"));
        saleMoveProduct1.setTotalPrice(new BigDecimal("2000.00"));
        saleMoveProduct1.setMovementDate(LocalDate.of(2025, 1, 15));

        purchaseMoveProduct1 = new Kardex();
        purchaseMoveProduct1.setId(java.util.UUID.randomUUID());
        purchaseMoveProduct1.setProductId("PROD-001");
        purchaseMoveProduct1.setTypeMovement(TypeMove.INCOME);
        purchaseMoveProduct1.setQuantity(5);
        purchaseMoveProduct1.setUnitPrice(new BigDecimal("800.00"));
        purchaseMoveProduct1.setTotalPrice(new BigDecimal("4000.00"));
        purchaseMoveProduct1.setMovementDate(LocalDate.of(2025, 1, 10));

        saleMoveProduct2 = new Kardex();
        saleMoveProduct2.setId(java.util.UUID.randomUUID());
        saleMoveProduct2.setProductId("PROD-002");
        saleMoveProduct2.setTypeMovement(TypeMove.OUTCOME);
        saleMoveProduct2.setQuantity(3);
        saleMoveProduct2.setUnitPrice(new BigDecimal("50.00"));
        saleMoveProduct2.setTotalPrice(new BigDecimal("150.00"));
        saleMoveProduct2.setMovementDate(LocalDate.of(2025, 1, 20));
    }

    @Test
    void earningsBetweenDatesDetailsProducts_ShouldReturnReportWithAllMovements() {
        List<Kardex> movements = Arrays.asList(saleMoveProduct1, purchaseMoveProduct1, saleMoveProduct2);
        when(kardexPersistancePort.findAllKardexByMovementDateBetween(startDate, endDate)).thenReturn(movements);
        when(productFeignClient.getProductByCod("PROD-001")).thenReturn(product1);
        when(productFeignClient.getProductByCod("PROD-002")).thenReturn(product2);

        ReportInventoryResponse result = earningsReport.earningsBetweenDatesDetailsProducts(startDate, endDate);

        assertNotNull(result);
        assertEquals(new BigDecimal("2150.00"), result.getTotalEarnings()); // 2000 + 150
        assertEquals(new BigDecimal("4000.00"), result.getTotalCosts());
        assertEquals(new BigDecimal("-1850.00"), result.getNetProfit()); // 2150 - 4000
        assertEquals(5, result.getTotalProductsSold()); // 2 + 3
        assertEquals(2, result.getTopSellingProducts().size());
        assertEquals(2, result.getEarningsByCategory().size());
        assertEquals(startDate, result.getStartDate());
        assertEquals(endDate, result.getEndDate());

        assertEquals(new BigDecimal("2000.00"), result.getEarningsByCategory().get("Electronics"));
        assertEquals(new BigDecimal("150.00"), result.getEarningsByCategory().get("Clothing"));

        verify(kardexPersistancePort).findAllKardexByMovementDateBetween(startDate, endDate);
        verify(productFeignClient, times(1)).getProductByCod("PROD-001");
        verify(productFeignClient).getProductByCod("PROD-002");
    }

    @Test
    void earningsBetweenDatesDetailsProducts_WithNoMovements_ShouldReturnEmptyReport() {
        when(kardexPersistancePort.findAllKardexByMovementDateBetween(startDate, endDate)).thenReturn(List.of());

        ReportInventoryResponse result = earningsReport.earningsBetweenDatesDetailsProducts(startDate, endDate);

        assertNotNull(result);
        assertEquals(BigDecimal.ZERO, result.getTotalEarnings());
        assertEquals(BigDecimal.ZERO, result.getTotalCosts());
        assertEquals(BigDecimal.ZERO, result.getNetProfit());
        assertEquals(0, result.getTotalProductsSold());
        assertTrue(result.getTopSellingProducts().isEmpty());
        assertTrue(result.getEarningsByCategory().isEmpty());
        assertEquals(startDate, result.getStartDate());
        assertEquals(endDate, result.getEndDate());

        verify(kardexPersistancePort).findAllKardexByMovementDateBetween(startDate, endDate);
        verifyNoInteractions(productFeignClient);
    }

    @Test
    void earningsBetweenDatesDetailsProducts_ShouldCalculateProfitMargin() {
        List<Kardex> movements = Arrays.asList(saleMoveProduct1, purchaseMoveProduct1);
        when(kardexPersistancePort.findAllKardexByMovementDateBetween(startDate, endDate)).thenReturn(movements);
        when(productFeignClient.getProductByCod("PROD-001")).thenReturn(product1);
        ReportInventoryResponse result = earningsReport.earningsBetweenDatesDetailsProducts(startDate, endDate);

        assertNotNull(result);
        assertEquals(1, result.getTopSellingProducts().size());
        ReportInventoryResponse.ProductSalesDetail productDetail = result.getTopSellingProducts().getFirst();
        assertEquals(product1, productDetail.getProduct());
        assertEquals(2, productDetail.getQuantitySold());
        assertEquals(new BigDecimal("2000.00"), productDetail.getTotalRevenue());

        BigDecimal expectedMargin = new BigDecimal("20.0000");
        assertEquals(0, expectedMargin.compareTo(productDetail.getProfitMargin()));

        verify(kardexPersistancePort).findAllKardexByMovementDateBetween(startDate, endDate);
        verify(productFeignClient).getProductByCod("PROD-001");
    }

    @Test
    void earningsBetweenDatesDetailsProducts_WithOnlySalesNoIncome_ShouldHaveZeroProfitMargin() {
        List<Kardex> movements = List.of(saleMoveProduct1);
        when(kardexPersistancePort.findAllKardexByMovementDateBetween(startDate, endDate)).thenReturn(movements);
        when(productFeignClient.getProductByCod("PROD-001")).thenReturn(product1);

        ReportInventoryResponse result = earningsReport.earningsBetweenDatesDetailsProducts(startDate, endDate);

        assertNotNull(result);
        assertEquals(1, result.getTopSellingProducts().size());

        ReportInventoryResponse.ProductSalesDetail productDetail = result.getTopSellingProducts().getFirst();
        assertEquals(product1, productDetail.getProduct());
        assertEquals(2, productDetail.getQuantitySold());
        assertEquals(new BigDecimal("2000.00"), productDetail.getTotalRevenue());
        assertEquals(BigDecimal.ZERO, productDetail.getProfitMargin());

        verify(kardexPersistancePort).findAllKardexByMovementDateBetween(startDate, endDate);
        verify(productFeignClient).getProductByCod("PROD-001");
    }

    @Test
    void earningsBetweenDatesDetailsProducts_WithOnlyIncome_ShouldNotAppearInTopSellingProducts() {
        List<Kardex> movements = List.of(purchaseMoveProduct1);  // Only purchases, no sales
        when(kardexPersistancePort.findAllKardexByMovementDateBetween(startDate, endDate)).thenReturn(movements);

        ReportInventoryResponse result = earningsReport.earningsBetweenDatesDetailsProducts(startDate, endDate);

        assertNotNull(result);
        assertEquals(0, result.getTopSellingProducts().size());
        assertEquals(BigDecimal.ZERO, result.getTotalEarnings());
        assertEquals(new BigDecimal("4000.00"), result.getTotalCosts());
        assertEquals(new BigDecimal("-4000.00"), result.getNetProfit());

        verify(kardexPersistancePort).findAllKardexByMovementDateBetween(startDate, endDate);
        verifyNoInteractions(productFeignClient);
    }

    @Test
    void earningsBetweenDatesDetailsProducts_WithMultipleSalesOfSameProduct_ShouldCombineQuantities() {
        Kardex anotherSaleMoveProduct1 = new Kardex();
        anotherSaleMoveProduct1.setId(java.util.UUID.randomUUID());
        anotherSaleMoveProduct1.setProductId("PROD-001");
        anotherSaleMoveProduct1.setTypeMovement(TypeMove.OUTCOME);
        anotherSaleMoveProduct1.setQuantity(3);
        anotherSaleMoveProduct1.setUnitPrice(new BigDecimal("1000.00"));
        anotherSaleMoveProduct1.setTotalPrice(new BigDecimal("3000.00"));
        anotherSaleMoveProduct1.setMovementDate(LocalDate.of(2025, 1, 16));

        List<Kardex> movements = Arrays.asList(saleMoveProduct1, anotherSaleMoveProduct1, purchaseMoveProduct1);
        when(kardexPersistancePort.findAllKardexByMovementDateBetween(startDate, endDate)).thenReturn(movements);
        when(productFeignClient.getProductByCod("PROD-001")).thenReturn(product1);
        ReportInventoryResponse result = earningsReport.earningsBetweenDatesDetailsProducts(startDate, endDate);
        assertNotNull(result);
        assertEquals(1, result.getTopSellingProducts().size());

        ReportInventoryResponse.ProductSalesDetail productDetail = result.getTopSellingProducts().getFirst();
        assertEquals(product1, productDetail.getProduct());
        assertEquals(5, productDetail.getQuantitySold());  // 2 + 3
        assertEquals(new BigDecimal("5000.00"), productDetail.getTotalRevenue());  // 2000 + 3000

        BigDecimal expectedMargin = new BigDecimal("20.0000");
        assertEquals(0, expectedMargin.compareTo(productDetail.getProfitMargin()));
        verify(kardexPersistancePort).findAllKardexByMovementDateBetween(startDate, endDate);
        verify(productFeignClient, times(2)).getProductByCod("PROD-001");
    }

    @Test
    void earningsBetweenDatesDetailsProducts_ShouldLimitTopSellingProductsToTen() {
        List<Kardex> movements = new java.util.ArrayList<>();

        for (int i = 1; i <= 15; i++) {
            String productId = "PROD-" + String.format("%03d", i);

            Product product = new Product();
            product.setCod(productId);
            product.setName("Product " + i);
            product.setCategory(category1);

            Kardex saleMove = new Kardex();
            saleMove.setId(java.util.UUID.randomUUID());
            saleMove.setProductId(productId);
            saleMove.setTypeMovement(TypeMove.OUTCOME);
            saleMove.setQuantity(1);
            saleMove.setUnitPrice(new BigDecimal(i * 100));
            saleMove.setTotalPrice(new BigDecimal(i * 100));
            saleMove.setMovementDate(LocalDate.of(2025, 1, i));

            movements.add(saleMove);

            when(productFeignClient.getProductByCod(productId)).thenReturn(product);
        }

        when(kardexPersistancePort.findAllKardexByMovementDateBetween(startDate, endDate)).thenReturn(movements);

        ReportInventoryResponse result = earningsReport.earningsBetweenDatesDetailsProducts(startDate, endDate);

        assertNotNull(result);
        assertEquals(10, result.getTopSellingProducts().size());

        for (int i = 0; i < 9; i++) {
            BigDecimal currentRevenue = result.getTopSellingProducts().get(i).getTotalRevenue();
            BigDecimal nextRevenue = result.getTopSellingProducts().get(i + 1).getTotalRevenue();
            assertTrue(currentRevenue.compareTo(nextRevenue) >= 0,
                    "Products should be sorted by revenue in descending order");
        }

        verify(kardexPersistancePort).findAllKardexByMovementDateBetween(startDate, endDate);
    }
}