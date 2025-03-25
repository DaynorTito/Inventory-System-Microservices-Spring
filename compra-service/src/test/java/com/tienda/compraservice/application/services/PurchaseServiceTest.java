package com.tienda.compraservice.application.services;

import com.tienda.compraservice.application.mapper.PurchaseDomainMapper;
import com.tienda.compraservice.application.validator.FeignValidator;
import com.tienda.compraservice.domain.model.dto.Provider;
import com.tienda.compraservice.domain.model.dto.Purchase;
import com.tienda.compraservice.domain.model.dto.request.CreatePurchaseRequest;
import com.tienda.compraservice.domain.model.dto.request.DetailPurchaseRequest;
import com.tienda.compraservice.domain.model.dto.request.PurchaseInventoryRequest;
import com.tienda.compraservice.domain.model.dto.response.DetailPurchaseResponse;
import com.tienda.compraservice.domain.model.dto.response.ProviderResponse;
import com.tienda.compraservice.domain.model.dto.response.PurchasesResponse;
import com.tienda.compraservice.domain.port.ProviderPersistancePort;
import com.tienda.compraservice.domain.port.PurchasePersistancePort;
import com.tienda.compraservice.infraestructure.adapters.exception.ProviderNotFoundException;
import com.tienda.compraservice.infraestructure.adapters.exception.PurchaseNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

class PurchaseServiceTest {

    @Mock
    private PurchasePersistancePort purchasePersistancePort;

    @Mock
    private ProviderPersistancePort providerPersistancePort;

    @Mock
    private PurchaseDomainMapper purchaseDomainMapper;

    @Mock
    private FeignValidator feignValidator;

    @InjectMocks
    private PurchaseService purchaseService;

    private Provider mockProvider;
    private Purchase mockPurchase;
    private PurchasesResponse mockPurchaseResponse;
    private CreatePurchaseRequest mockCreateRequest;
    private UUID purchaseId;

    @BeforeEach
    void setUp() {
        purchaseId = UUID.randomUUID();
        UUID providerId = UUID.randomUUID();

        mockProvider = Provider.builder()
                .id(providerId)
                .name("Test Provider")
                .address("Test Address")
                .phone("1234567890")
                .email("test@provider.com")
                .active(true)
                .build();

        List<DetailPurchaseResponse> detailsList = new ArrayList<>();
        DetailPurchaseResponse detail = DetailPurchaseResponse.builder()
                .id(UUID.randomUUID())
                .productId("PROD001")
                .quantity(10)
                .unitPrice(new BigDecimal("15.00"))
                .expirationDate(LocalDate.now().plusMonths(6))
                .build();
        detailsList.add(detail);

        ProviderResponse providerResponse = ProviderResponse.builder()
                .id(providerId)
                .name("Test Provider")
                .address("Test Address")
                .phone("1234567890")
                .email("test@provider.com")
                .active(true)
                .build();

        mockPurchase = Purchase.builder()
                .id(purchaseId)
                .provider(providerResponse)
                .items(detailsList)
                .total(new BigDecimal("150.00"))
                .canceled(false)
                .adquisitionDate(LocalDateTime.now())
                .build();

        mockPurchaseResponse = PurchasesResponse.builder()
                .id(purchaseId)
                .provider(providerResponse)
                .items(detailsList)
                .total(new BigDecimal("150.00"))
                .canceled(false)
                .adquisitionDate(LocalDateTime.now())
                .build();

        List<DetailPurchaseRequest> detailRequests = new ArrayList<>();
        DetailPurchaseRequest detailRequest = DetailPurchaseRequest.builder()
                .productId("PROD001")
                .quantity(10)
                .unitPrice(new BigDecimal("15.00"))
                .expirationDate(LocalDate.now().plusMonths(6))
                .build();
        detailRequests.add(detailRequest);

        mockCreateRequest = CreatePurchaseRequest.builder()
                .provider("Test Provider")
                .items(detailRequests)
                .build();
    }

    @Test
    void createPurchaseFromExistinProducst_Success() {
        when(providerPersistancePort.findProviderByName("Test Provider")).thenReturn(mockProvider);
        when(purchaseDomainMapper.createPurchaseRequestToDomain(mockCreateRequest, mockProvider))
                .thenReturn(mockPurchase);
        when(purchasePersistancePort.create(any(Purchase.class))).thenReturn(mockPurchase);
        when(purchaseDomainMapper.domainToResponse(mockPurchase)).thenReturn(mockPurchaseResponse);
        doNothing().when(feignValidator).verifyProductIntoService(anyString());
        doNothing().when(feignValidator).registerPurchase(any(PurchaseInventoryRequest.class));

        PurchasesResponse result = purchaseService.createPurchaseFromExistinProducst(mockCreateRequest);

        assertNotNull(result);
        assertEquals(purchaseId, result.getId());
        assertEquals(new BigDecimal("150.00"), result.getTotal());
        assertEquals("Test Provider", result.getProvider().getName());
        assertEquals(1, result.getItems().size());

        verify(purchaseDomainMapper).createPurchaseRequestToDomain(mockCreateRequest, mockProvider);
        verify(purchasePersistancePort).create(any(Purchase.class));
        verify(purchaseDomainMapper).domainToResponse(mockPurchase);
        verify(feignValidator).verifyProductIntoService("PROD001");
        verify(feignValidator).registerPurchase(any(PurchaseInventoryRequest.class));
    }

    @Test
    void createPurchaseFromExistinProducst_ProviderNotFound() {
        when(providerPersistancePort.findProviderByName("Test Provider")).thenReturn(null);

        assertThrows(ProviderNotFoundException.class, () -> {
            purchaseService.createPurchaseFromExistinProducst(mockCreateRequest);
        });

        verify(providerPersistancePort).findProviderByName("Test Provider");
        verifyNoInteractions(purchaseDomainMapper);
        verifyNoInteractions(purchasePersistancePort);
    }

    @Test
    void getPurchaseById_Success() {
        when(purchasePersistancePort.readById(purchaseId)).thenReturn(mockPurchase);
        when(purchaseDomainMapper.domainToResponse(mockPurchase)).thenReturn(mockPurchaseResponse);

        PurchasesResponse result = purchaseService.getPurchaseById(purchaseId);

        assertNotNull(result);
        assertEquals(purchaseId, result.getId());

        verify(purchasePersistancePort).readById(purchaseId);
        verify(purchaseDomainMapper).domainToResponse(mockPurchase);
    }

    @Test
    void getPurchaseById_NotFound() {
        when(purchasePersistancePort.readById(purchaseId)).thenReturn(null);
        assertThrows(PurchaseNotFoundException.class, () -> {
            purchaseService.getPurchaseById(purchaseId);
        });

        verify(purchasePersistancePort).readById(purchaseId);
        verifyNoInteractions(purchaseDomainMapper);
    }

    @Test
    void updatePurchase_Success() {
        when(purchasePersistancePort.readById(purchaseId)).thenReturn(mockPurchase);
        when(purchasePersistancePort.update(mockPurchase, purchaseId)).thenReturn(mockPurchase);
        when(purchaseDomainMapper.domainToResponse(mockPurchase)).thenReturn(mockPurchaseResponse);

        PurchasesResponse result = purchaseService.updatePurchase(purchaseId);

        assertNotNull(result);
        assertEquals(purchaseId, result.getId());
        verify(purchasePersistancePort).readById(purchaseId);
        verify(purchasePersistancePort).update(mockPurchase, purchaseId);
        verify(purchaseDomainMapper).domainToResponse(mockPurchase);
    }

    @Test
    void getAllPurchases_Success() {
        List<Purchase> purchases = new ArrayList<>();
        purchases.add(mockPurchase);

        when(purchasePersistancePort.findAllPurchases()).thenReturn(purchases);
        when(purchaseDomainMapper.domainToResponse(mockPurchase)).thenReturn(mockPurchaseResponse);

        List<PurchasesResponse> results = purchaseService.getAllPurchases();

        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(purchaseId, results.getFirst().getId());

        verify(purchasePersistancePort).findAllPurchases();
        verify(purchaseDomainMapper).domainToResponse(mockPurchase);
    }

    @Test
    void deletePurchaseById_Success() {
        when(purchasePersistancePort.readById(purchaseId)).thenReturn(mockPurchase);
        doNothing().when(purchasePersistancePort).deleteById(purchaseId);

        purchaseService.deletePurchaseById(purchaseId);

        verify(purchasePersistancePort).readById(purchaseId);
        verify(purchasePersistancePort).deleteById(purchaseId);
    }

    @Test
    void deletePurchaseById_NotFound() {
        when(purchasePersistancePort.readById(purchaseId)).thenReturn(null);

        assertThrows(PurchaseNotFoundException.class, () -> {
            purchaseService.deletePurchaseById(purchaseId);
        });

        verify(purchasePersistancePort).readById(purchaseId);
        verify(purchasePersistancePort, never()).deleteById(any());
    }

    @Test
    void registerOnInventoryServiceFullCreation_Success() {
        when(providerPersistancePort.findProviderByName("Test Provider")).thenReturn(mockProvider);
        doNothing().when(feignValidator).registerPurchase(any(PurchaseInventoryRequest.class));

        purchaseService.registerOnInventoryServiceFullCreation(mockPurchaseResponse);

        verify(providerPersistancePort).findProviderByName("Test Provider");

        ArgumentCaptor<PurchaseInventoryRequest> requestCaptor =
                ArgumentCaptor.forClass(PurchaseInventoryRequest.class);
        verify(feignValidator).registerPurchase(requestCaptor.capture());

        PurchaseInventoryRequest capturedRequest = requestCaptor.getValue();
        assertEquals(10, capturedRequest.getQuantity());
        assertEquals(new BigDecimal("15.00"), capturedRequest.getPurchaseUnitCost());
        assertEquals(mockProvider.getId(), capturedRequest.getProviderId());
        assertEquals("PROD001", capturedRequest.getProductId());
    }
}
