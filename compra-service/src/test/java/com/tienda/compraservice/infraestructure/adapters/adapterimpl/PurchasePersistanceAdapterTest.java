package com.tienda.compraservice.infraestructure.adapters.adapterimpl;

import com.tienda.compraservice.domain.model.dto.Purchase;
import com.tienda.compraservice.domain.model.dto.response.DetailPurchaseResponse;
import com.tienda.compraservice.domain.model.dto.response.ProviderResponse;
import com.tienda.compraservice.infraestructure.adapters.entity.DetailPurchaseEntity;
import com.tienda.compraservice.infraestructure.adapters.entity.ProviderEntity;
import com.tienda.compraservice.infraestructure.adapters.entity.PurchaseEntity;
import com.tienda.compraservice.infraestructure.adapters.exception.PurchaseNotFoundException;
import com.tienda.compraservice.infraestructure.adapters.mapper.PurchaseMapper;
import com.tienda.compraservice.infraestructure.adapters.repository.DetailPurchaseRepository;
import com.tienda.compraservice.infraestructure.adapters.repository.ProviderRepository;
import com.tienda.compraservice.infraestructure.adapters.repository.PurchaseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PurchasePersistanceAdapterTest {

    @Mock
    private PurchaseRepository purchaseRepository;

    @Mock
    private DetailPurchaseRepository detailPurchaseRepository;

    @Mock
    private PurchaseMapper purchaseMapper;

    @Mock
    private ProviderRepository providerRepository;

    @InjectMocks
    private PurchasePersistanceAdapter purchasePersistanceAdapter;

    private Purchase purchase;
    private PurchaseEntity purchaseEntity;
    private DetailPurchaseEntity detailPurchaseEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        purchase = Purchase.builder()
                .id(UUID.randomUUID())
                .total(BigDecimal.valueOf(100))
                .canceled(false)
                .adquisitionDate(LocalDateTime.now())
                .provider(ProviderResponse.builder()
                        .id(UUID.randomUUID())
                        .name("Provider")
                        .address("Address")
                        .phone("123456789")
                        .email("provider@example.com")
                        .active(true)
                        .build())
                .items(Collections.singletonList(DetailPurchaseResponse.builder()
                        .id(UUID.randomUUID())
                        .productId("cod")
                        .quantity(10)
                        .unitPrice(BigDecimal.valueOf(10))
                        .expirationDate(LocalDate.now().plusDays(30))
                        .build()))
                .build();

        purchaseEntity = PurchaseEntity.builder()
                .id(purchase.getId())
                .total(purchase.getTotal())
                .canceled(purchase.getCanceled())
                .adquisitionDate(purchase.getAdquisitionDate())
                .provider(new ProviderEntity())
                .items(new ArrayList<>())
                .build();

        detailPurchaseEntity = DetailPurchaseEntity.builder()
                .id(UUID.randomUUID())
                .productId("cod")
                .quantity(10)
                .unitPrice(BigDecimal.valueOf(10))
                .expirationDate(LocalDate.now().plusDays(30))
                .purchase(purchaseEntity)
                .build();
    }

    @Test
    void create_ShouldReturnPurchase_WhenPurchaseIsValid() {
        when(purchaseRepository.save(any(PurchaseEntity.class))).thenReturn(purchaseEntity);
        when(detailPurchaseRepository.saveAll(anyList())).thenReturn(Collections.singletonList(detailPurchaseEntity));
        when(purchaseMapper.toDomain(any(PurchaseEntity.class))).thenReturn(purchase);
        Purchase result = purchasePersistanceAdapter.create(purchase);
        assertNotNull(result);
        assertEquals(purchase.getId(), result.getId());
        assertEquals(purchase.getTotal(), result.getTotal());
    }

    @Test
    void readById_ShouldReturnPurchase_WhenPurchaseFound() {
        when(purchaseRepository.findById(any(UUID.class))).thenReturn(Optional.of(purchaseEntity));
        when(purchaseMapper.toDomain(any(PurchaseEntity.class))).thenReturn(purchase);
        Purchase result = purchasePersistanceAdapter.readById(UUID.randomUUID());
        assertNotNull(result);
        assertEquals(purchase.getId(), result.getId());
        assertEquals(purchase.getTotal(), result.getTotal());
    }

    @Test
    void readById_ShouldThrowPurchaseNotFoundException_WhenPurchaseNotFound() {
        when(purchaseRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        assertThrows(PurchaseNotFoundException.class, () -> purchasePersistanceAdapter.readById(UUID.randomUUID()));
    }

    @Test
    void update_ShouldReturnUpdatedPurchase_WhenPurchaseExists() {
        when(purchaseRepository.findById(any(UUID.class))).thenReturn(Optional.of(purchaseEntity));
        when(purchaseRepository.save(any(PurchaseEntity.class))).thenReturn(purchaseEntity);
        when(purchaseMapper.toDomain(any(PurchaseEntity.class))).thenReturn(purchase);
        Purchase updatedPurchase = purchasePersistanceAdapter.update(purchase, purchase.getId());
        assertNotNull(updatedPurchase);
        assertEquals(purchase.getId(), updatedPurchase.getId());
    }

    @Test
    void update_ShouldReturnNull_WhenPurchaseNotFound() {
        when(purchaseRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        Purchase updatedPurchase = purchasePersistanceAdapter.update(purchase, UUID.randomUUID());
        assertNull(updatedPurchase);
    }

    @Test
    void deleteById_ShouldDeletePurchase_WhenPurchaseExists() {
        doNothing().when(purchaseRepository).deleteById(any(UUID.class));
        purchasePersistanceAdapter.deleteById(UUID.randomUUID());
        verify(purchaseRepository, times(1)).deleteById(any(UUID.class));
    }

    @Test
    void findAllPurchases_ShouldReturnListOfPurchases() {
        when(purchaseRepository.findAll()).thenReturn(Collections.singletonList(purchaseEntity));
        when(purchaseMapper.toDomain(any(PurchaseEntity.class))).thenReturn(purchase);

        List<Purchase> result = purchasePersistanceAdapter.findAllPurchases();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(purchase.getId(), result.getFirst().getId());
    }

    @Test
    void create_ShouldReturnPurchase_WhenItemsAreMappedCorrectly() {
        when(purchaseRepository.save(any(PurchaseEntity.class))).thenReturn(purchaseEntity);
        when(detailPurchaseRepository.saveAll(anyList())).thenReturn(Collections.singletonList(detailPurchaseEntity));
        when(purchaseMapper.toDomain(any(PurchaseEntity.class))).thenReturn(purchase);
        Purchase result = purchasePersistanceAdapter.create(purchase);
        assertNotNull(result);
        assertEquals(purchase.getItems().size(), result.getItems().size());
        assertEquals(purchase.getItems().getFirst().getProductId(), result.getItems().getFirst().getProductId());
    }
}