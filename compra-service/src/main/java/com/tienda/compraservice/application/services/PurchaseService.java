package com.tienda.compraservice.application.services;

import com.tienda.compraservice.application.mapper.PurchaseDomainMapper;
import com.tienda.compraservice.application.validator.FeignValidator;
import com.tienda.compraservice.domain.model.dto.Purchase;
import com.tienda.compraservice.domain.model.dto.request.CreateCompletePurchase;
import com.tienda.compraservice.domain.model.dto.request.CreatePurchaseRequest;
import com.tienda.compraservice.domain.model.dto.request.PurchaseInventoryRequest;
import com.tienda.compraservice.domain.model.dto.response.DetailPurchaseResponse;
import com.tienda.compraservice.domain.model.dto.response.PurchasesResponse;
import com.tienda.compraservice.domain.port.ProviderPersistancePort;
import com.tienda.compraservice.domain.port.PurchasePersistancePort;
import com.tienda.compraservice.application.useCases.PurchaseUseCases;
import com.tienda.compraservice.infraestructure.adapters.exception.ProviderNotFoundException;
import com.tienda.compraservice.infraestructure.adapters.exception.PurchaseNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PurchaseService implements PurchaseUseCases {

    private final PurchasePersistancePort purchasePersistancePort;
    private final ProviderPersistancePort providerPersistancePort;
    private final PurchaseDomainMapper purchaseDomainMapper;
    private final FeignValidator feignValidator;

    /**
     * Creates a purchase from existing products by verifying the provider and calculating the total cost
     *
     * @param request The purchase request containing the provider and items
     * @return The created purchase response
     */
    @Override
    public PurchasesResponse createPurchaseFromExistinProducst(CreatePurchaseRequest request) {
        var provider = providerPersistancePort.findProviderByName(request.getProvider());
        if (provider == null) throw new ProviderNotFoundException("Proveedor no encontrado");
        var purchaseDomain = purchaseDomainMapper.createPurchaseRequestToDomain(request, provider);
        BigDecimal total = BigDecimal.ZERO;
        for (var item : request.getItems()) {
            feignValidator.verifyProductIntoService(item.getProductId());
            BigDecimal itemTotal = item.getUnitPrice().multiply(new BigDecimal(item.getQuantity()));
            total = total.add(itemTotal);
        }
        return getPurchasesResponse(purchaseDomain, total);
    }

    /**
     * Returns a purchase response with the total cost and other details
     *
     * @param purchaseDomain The purchase domain object
     * @param total The total cost of the purchase
     * @return The purchase response
     */
    private PurchasesResponse getPurchasesResponse(Purchase purchaseDomain, BigDecimal total) {
        purchaseDomain.setTotal(total);
        purchaseDomain.setCanceled(false);
        purchaseDomain.setAdquisitionDate(LocalDateTime.now());
        var purchaseCreated = purchasePersistancePort.create(purchaseDomain);
        var purchaseFormated = purchaseDomainMapper.domainToResponse(purchaseCreated);
        registerOnInventoryServiceFullCreation(purchaseFormated);
        return purchaseFormated;
    }

    /**
     * Creates a purchase from new products, validates provider, and calculates the total cost
     *
     * @param request The complete purchase request
     * @return The created purchase response
     */
    @Override
    public PurchasesResponse createPurchaseFromNewProducts(CreateCompletePurchase request) {
        var provider = providerPersistancePort.findProviderByName(request.getProvider());
        if (provider == null) throw new ProviderNotFoundException("Proveedor no encontrado");
        var productResponses = new ArrayList<String>();
        for (var item : request.getItems()) {
            var prod = feignValidator.createProductIntoService(item.getProduct());
            productResponses.add(prod.getCod());
        }
        var purchaseDomain = purchaseDomainMapper.createCompletePurchaseToDomain(request, provider, productResponses);
        BigDecimal total = BigDecimal.ZERO;
        for (int i = 0; i < purchaseDomain.getItems().size(); i++) {
            var item = purchaseDomain.getItems().get(i);
            BigDecimal itemTotal = item.getUnitPrice().multiply(new BigDecimal(item.getQuantity()));
            total = total.add(itemTotal);
        }
        return getPurchasesResponse(purchaseDomain, total);
    }

    /**
     * Updates an existing purchase by its ID.]
     *
     * @param purchaseId The ID of the purchase to update.]
     * @return The updated purchase response
     */
    @Override
    public PurchasesResponse updatePurchase(UUID purchaseId) {
        var purchase = validateExistPurchase(purchaseId);
        var updatedPurchase = purchasePersistancePort.update(purchase, purchaseId);
        return purchaseDomainMapper.domainToResponse(updatedPurchase);
    }

    /**
     * Retrieves a purchase by its ID
     *
     * @param purchaseId The ID of the purchase to retrieve
     * @return The purchase response
     */
    @Override
    public PurchasesResponse getPurchaseById(UUID purchaseId) {
        var purchase = validateExistPurchase(purchaseId);
        return purchaseDomainMapper.domainToResponse(purchase);
    }

    /**
     * Retrieves all purchases
     *
     * @return A list of all purchase responses
     */
    @Override
    public List<PurchasesResponse> getAllPurchases() {
        var purchases = purchasePersistancePort.findAllPurchases();
        return purchases.stream()
                .map(purchaseDomainMapper::domainToResponse)
                .toList();
    }

    /**
     * Deletes a purchase by its ID
     *
     * @param purchaseId The ID of the purchase to delete
     */
    @Override
    public void deletePurchaseById(UUID purchaseId) {
        validateExistPurchase(purchaseId);
        purchasePersistancePort.deleteById(purchaseId);
    }

    /**
     * Registers the purchase in the inventory service after full creation
     *
     * @param response The purchase response
     */
    public void registerOnInventoryServiceFullCreation(PurchasesResponse response) {
        List<DetailPurchaseResponse> listDetailsOregister = response.getItems();
        var provider = providerPersistancePort.findProviderByName(response.getProvider().getName());
        for (DetailPurchaseResponse item : listDetailsOregister) {
            var request = PurchaseInventoryRequest.builder()
                    .quantity(item.getQuantity())
                    .purchaseUnitCost(item.getUnitPrice())
                    .providerId(provider.getId())
                    .productId(item.getProductId())
                    .expiryDate(item.getExpirationDate()).build();
            feignValidator.registerPurchase(request);
        }
    }

    /**
     * Validates if a purchase exists by its ID
     *
     * @param purchaseId The ID of the purchase to validate
     * @return The purchase object
     * @throws PurchaseNotFoundException if the purchase does not exist
     */
    private Purchase validateExistPurchase(UUID purchaseId) {
        var purchase = purchasePersistancePort.readById(purchaseId);
        if (purchase == null) {
            throw new PurchaseNotFoundException("Compra no encontrada");
        }
        return purchase;
    }
}
