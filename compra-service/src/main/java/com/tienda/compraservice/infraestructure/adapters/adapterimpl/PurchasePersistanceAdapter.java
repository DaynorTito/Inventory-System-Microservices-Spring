package com.tienda.compraservice.infraestructure.adapters.adapterimpl;

import com.tienda.compraservice.domain.model.dto.Purchase;
import com.tienda.compraservice.domain.model.dto.response.DetailPurchaseResponse;
import com.tienda.compraservice.domain.model.dto.response.ProviderResponse;
import com.tienda.compraservice.domain.port.PurchasePersistancePort;
import com.tienda.compraservice.infraestructure.adapters.entity.DetailPurchaseEntity;
import com.tienda.compraservice.infraestructure.adapters.entity.ProviderEntity;
import com.tienda.compraservice.infraestructure.adapters.entity.PurchaseEntity;
import com.tienda.compraservice.infraestructure.adapters.exception.PurchaseNotFoundException;
import com.tienda.compraservice.infraestructure.adapters.mapper.PurchaseMapper;
import com.tienda.compraservice.infraestructure.adapters.repository.DetailPurchaseRepository;
import com.tienda.compraservice.infraestructure.adapters.repository.ProviderRepository;
import com.tienda.compraservice.infraestructure.adapters.repository.PurchaseRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class PurchasePersistanceAdapter implements PurchasePersistancePort {
    private final PurchaseRepository purchaseRepository;
    private final DetailPurchaseRepository detailPurchaseRepository;
    private final PurchaseMapper purchaseMapper;
    private final ProviderRepository providerRepository;

    /**
     * Finds a purchase by its ID
     *
     * @param uuid The UUID of the purchase
     * @return The purchase found
     */
    @Override
    public Purchase readById(UUID uuid) {
        var purchase = purchaseRepository.findById(uuid);
        return purchase.map(purchaseMapper::toDomain).orElseThrow(
                () -> new PurchaseNotFoundException("Compra no encontrada"));
    }

    /**
     * Creates a new purchase
     *
     * @param purchase The purchase to create
     * @return The created purchase
     */
    @Override
    @Transactional
    public Purchase create(Purchase purchase) {
        var purchaseEntity = convertToEntity(purchase);
        purchaseEntity = purchaseRepository.save(purchaseEntity);
        List<DetailPurchaseEntity> detailEntities = new ArrayList<>();
        for (var detailResponse : purchase.getItems()) {
            DetailPurchaseEntity detailEntity = DetailPurchaseEntity.builder()
                    .productId(detailResponse.getProductId())
                    .quantity(detailResponse.getQuantity())
                    .unitPrice(detailResponse.getUnitPrice())
                    .expirationDate(detailResponse.getExpirationDate())
                    .purchase(purchaseEntity).build();
            detailEntities.add(detailEntity);
        }
        detailEntities = detailPurchaseRepository.saveAll(detailEntities);
        purchaseEntity.setItems(detailEntities);
        return convertToDomain(purchaseEntity);
    }

    /**
     * Updates an existing purchase
     *
     * @param purchase The purchase details to update
     * @param id The UUID of the purchase to update
     * @return The updated purchase
     */
    @Override
    @Transactional
    public Purchase update(Purchase purchase, UUID id) {
        var purchaseEntity = purchaseRepository.findById(id).orElse(null);
        if (purchaseEntity != null) {
            purchaseEntity.setCanceled(purchase.getCanceled());
            purchaseEntity = purchaseRepository.save(purchaseEntity);
            return convertToDomain(purchaseEntity);
        }
        return null;
    }

    /**
     * Deletes a purchase by its ID
     *
     * @param uuid The UUID of the purchase to delete
     */
    @Override
    public void deleteById(UUID uuid) {
        purchaseRepository.deleteById(uuid);
    }

    /**
     * Finds all purchases
     *
     * @return A list of all purchases
     */
    public List<Purchase> findAllPurchases() {
        return purchaseRepository.findAll()
                .stream()
                .map(purchaseMapper::toDomain)
                .toList();
    }

    /**
     * Converts a purchase domain object to an entity
     *
     * @param purchase The purchase domain object
     * @return The corresponding purchase entity
     */
    private PurchaseEntity convertToEntity(Purchase purchase) {
        var providerEntity = providerRepository.findById(purchase.getProvider().getId()).orElse(null);
        return PurchaseEntity.builder()
                .total(purchase.getTotal())
                .provider(providerEntity)
                .canceled(purchase.getCanceled())
                .build();
    }

    /**
     * Converts a purchase entity to a domain object
     *
     * @param entity The purchase entity
     * @return The corresponding purchase domain object
     */
    private Purchase convertToDomain(PurchaseEntity entity) {
        var items = entity.getItems().stream()
                .map(detailEntity -> DetailPurchaseResponse.builder()
                        .id(detailEntity.getId())
                        .productId(detailEntity.getProductId())
                        .quantity(detailEntity.getQuantity())
                        .unitPrice(detailEntity.getUnitPrice())
                        .expirationDate(detailEntity.getExpirationDate())
                        .build())
                .toList();

        var provider = ProviderResponse.builder()
                .id(entity.getProvider().getId())
                .name(entity.getProvider().getName())
                .address(entity.getProvider().getAddress())
                .phone(entity.getProvider().getPhone())
                .email(entity.getProvider().getEmail())
                .active(entity.getProvider().getActive())
                .build();

        return Purchase.builder()
                .id(entity.getId())
                .total(entity.getTotal())
                .provider(provider)
                .items(items)
                .canceled(entity.getCanceled())
                .adquisitionDate(entity.getAdquisitionDate())
                .build();
    }
}
