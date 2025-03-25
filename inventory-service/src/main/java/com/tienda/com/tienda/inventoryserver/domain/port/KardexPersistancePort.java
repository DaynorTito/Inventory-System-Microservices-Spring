package com.tienda.com.tienda.inventoryserver.domain.port;

import com.tienda.com.tienda.inventoryserver.domain.abstraction.PersistancePort;
import com.tienda.com.tienda.inventoryserver.domain.model.constant.TypeMove;
import com.tienda.com.tienda.inventoryserver.domain.model.dto.Kardex;
import com.tienda.com.tienda.inventoryserver.domain.model.dto.TopSellingProduct;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface KardexPersistancePort extends PersistancePort<Kardex, UUID> {
    List<Kardex> findAllKardexByProductId(String productId);
    List<Kardex> findAllKardexByMovementDateBetween(LocalDate movementDateAfter, LocalDate movementDateBefore);
    List<Kardex> findAllKardexByTypeMovement(TypeMove typeMovement);
    List<Kardex> findAllKardexByMovementDateBetweenAndProductId(LocalDate after, LocalDate before, String productId);
    List<TopSellingProduct> findTopSellingProducts(LocalDate after, LocalDate before, int limit);
    BigDecimal calculateEarningsBetweenDates(LocalDate startDate, LocalDate endDate);
    Kardex getLastKardexByProductId(String productId);
}
