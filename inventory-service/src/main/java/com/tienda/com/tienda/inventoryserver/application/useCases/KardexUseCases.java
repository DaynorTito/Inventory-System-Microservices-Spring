package com.tienda.com.tienda.inventoryserver.application.useCases;

import com.tienda.com.tienda.inventoryserver.domain.abstraction.CrudService;
import com.tienda.com.tienda.inventoryserver.domain.model.dto.Product;
import com.tienda.com.tienda.inventoryserver.domain.model.dto.request.KardexRequest;
import com.tienda.com.tienda.inventoryserver.domain.model.dto.response.KardexResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface KardexUseCases extends CrudService<KardexRequest, KardexResponse, UUID> {
    List<KardexResponse> getProductHistory(String productId);
    List<KardexResponse> getInventoryMovements(LocalDate startDate, LocalDate endDate);
    List<KardexResponse> getInventoryMovements(LocalDate startDate, LocalDate endDate, String productId);
    List<Product> getMostSoldProductsReport(LocalDate startDate, LocalDate endDate, Integer limit);
    List<Product> getMostSoldProductsReport(Integer limit);
}
