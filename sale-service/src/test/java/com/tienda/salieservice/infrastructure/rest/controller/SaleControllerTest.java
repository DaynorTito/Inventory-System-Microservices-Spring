package com.tienda.salieservice.infrastructure.rest.controller;

import com.tienda.salieservice.application.services.SaleService;
import com.tienda.salieservice.domain.model.dto.Sale;
import com.tienda.salieservice.domain.model.dto.request.SaleRequest;
import com.tienda.salieservice.domain.model.dto.response.SaleResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SaleControllerTest {

    @Mock
    private SaleService saleService;

    @InjectMocks
    private SaleController saleController;

    private UUID saleId;
    private SaleRequest saleRequest;
    private SaleResponse saleResponse;
    private Sale sale;

    @BeforeEach
    void setUp() {
        saleId = UUID.randomUUID();
        saleRequest = new SaleRequest();
        saleResponse = new SaleResponse();
        sale = new Sale();
    }

    @Test
    void getSaleById_ShouldReturnSaleResponse() {
        when(saleService.getById(saleId)).thenReturn(saleResponse);
        ResponseEntity<SaleResponse> response = saleController.getSaleById(saleId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(saleResponse, response.getBody());
    }

    @Test
    void createSale_ShouldReturnCreatedSaleResponse() {
        when(saleService.createEntity(saleRequest)).thenReturn(saleResponse);
        ResponseEntity<SaleResponse> response = saleController.createSale(saleRequest);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(saleResponse, response.getBody());
    }

    @Test
    void updateSale_ShouldReturnUpdatedSaleResponse() {
        when(saleService.updateEntity(saleRequest, saleId)).thenReturn(saleResponse);
        ResponseEntity<SaleResponse> response = saleController.updateSale(saleId, saleRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(saleResponse, response.getBody());
    }

    @Test
    void deleteSale_ShouldReturnNoContent() {
        doNothing().when(saleService).deleteEntityById(saleId);
        ResponseEntity<Void> response = saleController.deleteSale(saleId);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void getSalesBetweenDates_ShouldReturnListOfSales() {
        LocalDate startDate = LocalDate.now().minusDays(10);
        LocalDate endDate = LocalDate.now();
        when(saleService.findAllSalesBetweenDates(startDate, endDate)).thenReturn(List.of(sale));
        ResponseEntity<List<Sale>> response = saleController.getSalesBetweenDates(startDate, endDate);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void getSalesByProductId_ShouldReturnListOfSales() {
        String productId = "prod-123";
        when(saleService.findAllSalesByProductId(productId)).thenReturn(List.of(sale));
        ResponseEntity<List<Sale>> response = saleController.getSalesByProductId(productId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().isEmpty());
    }
}