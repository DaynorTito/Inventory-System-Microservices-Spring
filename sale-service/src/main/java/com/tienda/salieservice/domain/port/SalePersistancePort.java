package com.tienda.salieservice.domain.port;

import com.tienda.salieservice.domain.abstractions.PersistancePort;
import com.tienda.salieservice.domain.model.dto.Sale;
import com.tienda.salieservice.infrastructure.adapters.entity.SaleEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface SalePersistancePort extends PersistancePort<Sale, UUID> {
    List<Sale> findAllSalesBetweenDates(LocalDate startDate, LocalDate endDate);
    List<Sale> findAllSalesByProductId(String productId);
    List<Sale> findAllSalesByProductId(String productId, LocalDate startDate, LocalDate endDate);
    List<Sale> findAllSalesByCustomer(String customerName);
    List<Sale> findAllSalesByCustomer(String customerName, LocalDate startDate, LocalDate endDate);
    List<Sale> findAllSalesGreatPriceThan(BigDecimal price);
    List<Sale> findAllSalesGreatPriceLess(BigDecimal price);
}
