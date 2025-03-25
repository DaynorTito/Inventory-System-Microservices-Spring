package com.tienda.compraservice.domain.port;

import com.tienda.compraservice.domain.abstractions.PersistancePort;
import com.tienda.compraservice.domain.model.dto.Purchase;

import java.util.List;
import java.util.UUID;

public interface PurchasePersistancePort extends PersistancePort<Purchase, UUID> {
    List<Purchase> findAllPurchases();
}
