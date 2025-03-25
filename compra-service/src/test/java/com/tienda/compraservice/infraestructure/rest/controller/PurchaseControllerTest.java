package com.tienda.compraservice.infraestructure.rest.controller;

import com.tienda.compraservice.application.services.PurchaseService;
import com.tienda.compraservice.domain.model.dto.request.CreateCompletePurchase;
import com.tienda.compraservice.domain.model.dto.request.CreatePurchaseRequest;
import com.tienda.compraservice.domain.model.dto.response.PurchasesResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PurchaseControllerTest {
    @Mock
    private PurchaseService purchaseService;

    @InjectMocks
    private PurchaseController purchaseController;

    private CreatePurchaseRequest createPurchaseRequest;
    private CreateCompletePurchase createCompletePurchase;
    private PurchasesResponse purchasesResponse;
    private UUID purchaseId;

    @BeforeEach
    void setUp() {
        purchaseId = UUID.randomUUID();
        createPurchaseRequest = new CreatePurchaseRequest();
        createCompletePurchase = new CreateCompletePurchase();
        purchasesResponse = new PurchasesResponse();
    }

    @Test
    void createPurchaseFromExistingProducts_ShouldReturnPurchasesResponse() {
        when(purchaseService.createPurchaseFromExistinProducst(any(CreatePurchaseRequest.class)))
                .thenReturn(purchasesResponse);

        ResponseEntity<PurchasesResponse> response = purchaseController.createPurchaseFromExistingProducts(createPurchaseRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(purchasesResponse, response.getBody());
        verify(purchaseService, times(1)).createPurchaseFromExistinProducst(any(CreatePurchaseRequest.class));
    }

    @Test
    void createPurchaseFromNewProducts_ShouldReturnPurchasesResponse() {
        when(purchaseService.createPurchaseFromNewProducts(any(CreateCompletePurchase.class)))
                .thenReturn(purchasesResponse);

        ResponseEntity<PurchasesResponse> response = purchaseController.createPurchaseFromNewProducts(createCompletePurchase);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(purchasesResponse, response.getBody());
        verify(purchaseService, times(1)).createPurchaseFromNewProducts(any(CreateCompletePurchase.class));
    }

    @Test
    void updatePurchase_ShouldReturnPurchasesResponse() {
        when(purchaseService.updatePurchase(any(UUID.class))).thenReturn(purchasesResponse);

        ResponseEntity<PurchasesResponse> response = purchaseController.updatePurchase(purchaseId);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(purchasesResponse, response.getBody());
        verify(purchaseService, times(1)).updatePurchase(any(UUID.class));
    }

    @Test
    void getPurchaseById_ShouldReturnPurchasesResponse() {
        when(purchaseService.getPurchaseById(any(UUID.class))).thenReturn(purchasesResponse);

        ResponseEntity<PurchasesResponse> response = purchaseController.getPurchaseById(purchaseId);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(purchasesResponse, response.getBody());
        verify(purchaseService, times(1)).getPurchaseById(any(UUID.class));
    }

    @Test
    void getAllPurchases_ShouldReturnListOfPurchasesResponse() {
        List<PurchasesResponse> purchasesList = Collections.singletonList(purchasesResponse);
        when(purchaseService.getAllPurchases()).thenReturn(purchasesList);

        ResponseEntity<List<PurchasesResponse>> response = purchaseController.getAllPurchases();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(purchasesList, response.getBody());
        verify(purchaseService, times(1)).getAllPurchases();
    }

    @Test
    void deletePurchaseById_ShouldReturnNoContent() {
        doNothing().when(purchaseService).deletePurchaseById(any(UUID.class));

        ResponseEntity<Void> response = purchaseController.deletePurchaseById(purchaseId);

        assertNotNull(response);
        assertEquals(204, response.getStatusCodeValue());
        verify(purchaseService, times(1)).deletePurchaseById(any(UUID.class));
    }
}
