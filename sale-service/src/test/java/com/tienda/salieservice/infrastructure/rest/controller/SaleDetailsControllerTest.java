package com.tienda.salieservice.infrastructure.rest.controller;


import com.tienda.salieservice.application.services.SaleDetailsService;
import com.tienda.salieservice.domain.model.dto.request.SaleDetailsRequest;
import com.tienda.salieservice.domain.model.dto.response.SaleDetailsResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SaleDetailsControllerTest {

    @Mock
    private SaleDetailsService saleDetailsService;

    @InjectMocks
    private SaleDetailsController saleDetailsController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getSaleDetailById_ShouldReturnSaleDetailsResponse() {
        Long id = 1L;
        SaleDetailsResponse response = new SaleDetailsResponse();
        when(saleDetailsService.getById(id)).thenReturn(response);

        ResponseEntity<SaleDetailsResponse> result = saleDetailsController.getSaleDetailById(id);

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    void createSaleDetail_ShouldReturnCreatedSaleDetailsResponse() {
        SaleDetailsRequest request = new SaleDetailsRequest();
        SaleDetailsResponse response = new SaleDetailsResponse();
        when(saleDetailsService.createEntity(request)).thenReturn(response);

        ResponseEntity<SaleDetailsResponse> result = saleDetailsController.createSaleDetail(request);

        assertNotNull(result);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    void updateSaleDetail_ShouldReturnUpdatedSaleDetailsResponse() {
        Long id = 1L;
        SaleDetailsRequest request = new SaleDetailsRequest();
        SaleDetailsResponse response = new SaleDetailsResponse();
        when(saleDetailsService.updateEntity(request, id)).thenReturn(response);

        ResponseEntity<SaleDetailsResponse> result = saleDetailsController.updateSaleDetail(id, request);

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    void deleteSaleDetail_ShouldReturnNoContent() {
        Long id = 1L;
        doNothing().when(saleDetailsService).deleteEntityById(id);

        ResponseEntity<Void> result = saleDetailsController.deleteSaleDetail(id);

        assertNotNull(result);
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(saleDetailsService, times(1)).deleteEntityById(id);
    }
}