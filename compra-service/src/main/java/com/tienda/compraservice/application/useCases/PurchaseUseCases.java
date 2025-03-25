package com.tienda.compraservice.application.useCases;

import com.tienda.compraservice.domain.model.dto.request.CreateCompletePurchase;
import com.tienda.compraservice.domain.model.dto.request.CreatePurchaseRequest;
import com.tienda.compraservice.domain.model.dto.response.PurchasesResponse;

import java.util.List;
import java.util.UUID;

public interface PurchaseUseCases {
    PurchasesResponse createPurchaseFromExistinProducst(CreatePurchaseRequest request);
    PurchasesResponse createPurchaseFromNewProducts(CreateCompletePurchase request);
    PurchasesResponse getPurchaseById(UUID purchase);
    List<PurchasesResponse> getAllPurchases();
    void deletePurchaseById(UUID purchase);
    PurchasesResponse updatePurchase(UUID purchase);
}
