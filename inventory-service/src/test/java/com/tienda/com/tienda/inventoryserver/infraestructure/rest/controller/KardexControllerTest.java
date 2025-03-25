package com.tienda.com.tienda.inventoryserver.infraestructure.rest.controller;

import com.tienda.com.tienda.inventoryserver.application.services.EarningsReport;
import com.tienda.com.tienda.inventoryserver.application.services.KardexService;
import com.tienda.com.tienda.inventoryserver.domain.model.dto.request.KardexRequest;
import com.tienda.com.tienda.inventoryserver.domain.model.dto.response.KardexResponse;
import com.tienda.com.tienda.inventoryserver.domain.model.dto.response.ReportInventoryResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)

class KardexControllerTest {
    @Mock
    private KardexService kardexService;

    @Mock
    private EarningsReport earningsReport;

    @InjectMocks
    private KardexController kardexController;

    private UUID uuid;
    private KardexRequest kardexRequest;
    private KardexResponse kardexResponse;

    @BeforeEach
    void setUp() {
        uuid = UUID.randomUUID();
        kardexRequest = new KardexRequest();
        kardexResponse = new KardexResponse();
    }

    @Test
    void getProductHistory_ShouldReturnListOfKardexResponses() {
        String productId = "123";
        when(kardexService.getProductHistory(productId)).thenReturn(List.of(kardexResponse));
        ResponseEntity<List<KardexResponse>> response = kardexController.getProductHistory(productId);
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getInventoryMovements_ShouldReturnListOfKardexResponses() {
        LocalDate startDate = LocalDate.now().minusDays(10);
        LocalDate endDate = LocalDate.now();
        when(kardexService.getInventoryMovements(startDate, endDate)).thenReturn(List.of(kardexResponse));
        ResponseEntity<List<KardexResponse>> response = kardexController.getInventoryMovements(startDate, endDate);
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getById_ShouldReturnKardexResponse() {
        when(kardexService.getById(uuid)).thenReturn(kardexResponse);
        ResponseEntity<KardexResponse> response = kardexController.getById(uuid);
        assertNotNull(response.getBody());
    }

    @Test
    void createEntity_ShouldReturnKardexResponse() {
        when(kardexService.createEntity(kardexRequest)).thenReturn(kardexResponse);
        ResponseEntity<KardexResponse> response = kardexController.createEntity(kardexRequest);
        assertNotNull(response.getBody());
    }

    @Test
    void updateEntity_ShouldReturnKardexResponse() {
        when(kardexService.updateEntity(kardexRequest, uuid)).thenReturn(kardexResponse);
        ResponseEntity<KardexResponse> response = kardexController.updateEntity(kardexRequest, uuid);
        assertNotNull(response.getBody());
    }

    @Test
    void deleteEntityById_ShouldReturnNoContent() {
        doNothing().when(kardexService).deleteEntityById(uuid);
        ResponseEntity<Void> response = kardexController.deleteEntityById(uuid);
        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    void earningsBetweenDatesDetailsProducts_ShouldReturnReport() {
        LocalDate startDate = LocalDate.now().minusMonths(1);
        LocalDate endDate = LocalDate.now();
        ReportInventoryResponse report = new ReportInventoryResponse();
        when(earningsReport.earningsBetweenDatesDetailsProducts(startDate, endDate)).thenReturn(report);
        ResponseEntity<ReportInventoryResponse> response = kardexController.earningsBetweenDatesDetailsProducts(startDate, endDate);
        assertNotNull(response.getBody());
    }
}
