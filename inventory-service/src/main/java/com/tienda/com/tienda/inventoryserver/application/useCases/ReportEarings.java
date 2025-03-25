package com.tienda.com.tienda.inventoryserver.application.useCases;

import com.tienda.com.tienda.inventoryserver.domain.model.dto.response.ReportInventoryResponse;

import java.time.LocalDate;

public interface ReportEarings {
    ReportInventoryResponse earningsBetweenDatesDetailsProducts(LocalDate startDate, LocalDate endDate);
}
