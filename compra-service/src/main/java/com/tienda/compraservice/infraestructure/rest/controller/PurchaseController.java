package com.tienda.compraservice.infraestructure.rest.controller;

import com.tienda.compraservice.application.services.PurchaseService;
import com.tienda.compraservice.domain.model.dto.request.CreateCompletePurchase;
import com.tienda.compraservice.domain.model.dto.request.CreatePurchaseRequest;
import com.tienda.compraservice.domain.model.dto.response.PurchasesResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/purchase")
@AllArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;

    /**
     * Creates a purchase from existing products
     *
     * @param request The request object containing the existing products for the purchase
     * @return A response containing the created purchase details
     */
    @PostMapping("/existing")
    public ResponseEntity<PurchasesResponse> createPurchaseFromExistingProducts(
            @RequestBody @Valid CreatePurchaseRequest request) {
        var response = purchaseService.createPurchaseFromExistinProducst(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Creates a purchase from new products
     *
     * @param request The request object containing the new products for the purchase
     * @return A response containing the created purchase details
     */
    @PostMapping("/new")
    public ResponseEntity<PurchasesResponse> createPurchaseFromNewProducts(
            @RequestBody @Valid CreateCompletePurchase request) {
        var response = purchaseService.createPurchaseFromNewProducts(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Updates an existing purchase
     *
     * @param purchaseId The ID of the purchase to update
     * @return A response containing the updated purchase details
     */
    @PutMapping("/{purchaseId}")
    public ResponseEntity<PurchasesResponse> updatePurchase(@PathVariable UUID purchaseId) {
        var response = purchaseService.updatePurchase(purchaseId);
        return ResponseEntity.ok(response);
    }

    /**
     * Gets a purchase by its ID
     *
     * @param purchaseId The ID of the purchase
     * @return A response containing the purchase details
     */
    @GetMapping("/{purchaseId}")
    public ResponseEntity<PurchasesResponse> getPurchaseById(@PathVariable UUID purchaseId) {
        var response = purchaseService.getPurchaseById(purchaseId);
        return ResponseEntity.ok(response);
    }

    /**
     * Gets a list of all purchases
     *
     * @return A response containing the list of all purchases
     */
    @GetMapping
    public ResponseEntity<List<PurchasesResponse>> getAllPurchases() {
        var response = purchaseService.getAllPurchases();
        return ResponseEntity.ok(response);
    }

    /**
     * Deletes a purchase by its ID
     *
     * @param purchaseId The ID of the purchase to delete
     * @return A response indicating the deletion was successful
     */
    @DeleteMapping("/{purchaseId}")
    public ResponseEntity<Void> deletePurchaseById(@PathVariable UUID purchaseId) {
        purchaseService.deletePurchaseById(purchaseId);
        return ResponseEntity.noContent().build();
    }
}
