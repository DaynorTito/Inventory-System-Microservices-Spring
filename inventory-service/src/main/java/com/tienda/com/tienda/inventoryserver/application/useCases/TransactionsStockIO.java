package com.tienda.com.tienda.inventoryserver.application.useCases;

import com.tienda.com.tienda.inventoryserver.domain.model.dto.request.PurchaseInventoryRequest;
import com.tienda.com.tienda.inventoryserver.domain.model.dto.request.SaleInventoryRequest;

public interface TransactionsStockIO {
    String registerInputInventory(PurchaseInventoryRequest request);
    String registerOutputInventory(SaleInventoryRequest request);
}
