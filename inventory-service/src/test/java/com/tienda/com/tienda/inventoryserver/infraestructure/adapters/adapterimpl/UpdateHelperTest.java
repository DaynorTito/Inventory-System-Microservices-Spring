package com.tienda.com.tienda.inventoryserver.infraestructure.adapters.adapterimpl;

import static org.junit.jupiter.api.Assertions.*;

import com.tienda.com.tienda.inventoryserver.domain.model.dto.Stock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

class UpdateHelperTest {
    private Stock sourceStock;
    private Stock targetStock;

    @BeforeEach
    void setUp() {
        sourceStock = Stock.builder()
                .id(UUID.randomUUID())
                .quantity(10)
                .purchaseUnitCost(new BigDecimal("15.50"))
                .totalPurchaseCost(new BigDecimal("155.00"))
                .providerId(UUID.randomUUID())
                .productId("prod-001")
                .purchaseDate(LocalDate.of(2023, 1, 1))
                .expiryDate(LocalDate.of(2024, 1, 1))
                .build();

        targetStock = Stock.builder()
                .id(UUID.randomUUID())
                .quantity(5)
                .purchaseUnitCost(new BigDecimal("10.00"))
                .totalPurchaseCost(new BigDecimal("50.00"))
                .providerId(UUID.randomUUID())
                .productId("prod-002")
                .purchaseDate(LocalDate.of(2022, 1, 1))
                .expiryDate(LocalDate.of(2023, 1, 1))
                .build();
    }

    @Test
    void testUpdateNonNullFields() {
        UpdateHelper.updateNonNullFields(sourceStock, targetStock);
        assertEquals(sourceStock.getQuantity(), targetStock.getQuantity(), "Quantity should be updated.");
        assertEquals(sourceStock.getPurchaseUnitCost(), targetStock.getPurchaseUnitCost(), "Purchase unit cost should be updated.");
        assertEquals(sourceStock.getTotalPurchaseCost(), targetStock.getTotalPurchaseCost(), "Total purchase cost should be updated.");
        assertEquals(sourceStock.getProviderId(), targetStock.getProviderId(), "Provider ID should be updated.");
        assertEquals(sourceStock.getProductId(), targetStock.getProductId(), "Product ID should be updated.");
        assertEquals(sourceStock.getPurchaseDate(), targetStock.getPurchaseDate(), "Purchase date should be updated.");
        assertEquals(sourceStock.getExpiryDate(), targetStock.getExpiryDate(), "Expiry date should be updated.");

    }

    @Test
    void testUpdateNonNullFieldsWithNullSource() {
        sourceStock = null;
        assertEquals(5, targetStock.getQuantity(), "Quantity should remain the same.");
        assertEquals(new BigDecimal("10.00"), targetStock.getPurchaseUnitCost(), "Purchase unit cost should remain the same.");
        assertEquals(new BigDecimal("50.00"), targetStock.getTotalPurchaseCost(), "Total purchase cost should remain the same.");
        assertEquals("prod-002", targetStock.getProductId(), "Product ID should remain the same.");
        assertEquals(LocalDate.of(2022, 1, 1), targetStock.getPurchaseDate(), "Purchase date should remain the same.");
        assertEquals(LocalDate.of(2023, 1, 1), targetStock.getExpiryDate(), "Expiry date should remain the same.");
    }
}
