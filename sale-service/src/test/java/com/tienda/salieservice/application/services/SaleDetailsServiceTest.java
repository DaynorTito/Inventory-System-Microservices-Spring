package com.tienda.salieservice.application.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.tienda.salieservice.application.mapper.SaleDetailsDomainMapper;
import com.tienda.salieservice.domain.model.dto.request.SaleDetailsRequest;
import com.tienda.salieservice.domain.model.dto.response.SaleDetailsResponse;
import com.tienda.salieservice.domain.model.dto.SaleDetails;
import com.tienda.salieservice.domain.port.SaleDetailsPersistancePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

@ExtendWith(MockitoExtension.class)
class SaleDetailsServiceTest {

    @Mock
    private SaleDetailsPersistancePort saleDetailsPersistancePort;

    @Mock
    private SaleDetailsDomainMapper saleDetailsDomainMapper;

    @InjectMocks
    private SaleDetailsService saleDetailsService;

    private SaleDetailsRequest request;
    private SaleDetails saleDetails;
    private SaleDetailsResponse response;

    @BeforeEach
    void setUp() {
        request = SaleDetailsRequest.builder()
                .productId("P123")
                .quantity(2)
                .unitPrice(BigDecimal.valueOf(100))
                .discount(BigDecimal.valueOf(10))
                .build();

        saleDetails = SaleDetails.builder()
                .id(1L)
                .productId("P123")
                .quantity(2)
                .unitPrice(BigDecimal.valueOf(90))
                .subtotal(BigDecimal.valueOf(180))
                .build();

        response = SaleDetailsResponse.builder()
                .id(1L)
                .productId("P123")
                .quantity(2)
                .unitPrice(BigDecimal.valueOf(90))
                .subtotal(BigDecimal.valueOf(180))
                .build();
    }

    @Test
    void testGetById() {
        when(saleDetailsPersistancePort.readById(1L)).thenReturn(saleDetails);
        when(saleDetailsDomainMapper.saleDetailsToSaleDetailsResponse(saleDetails)).thenReturn(response);

        SaleDetailsResponse result = saleDetailsService.getById(1L);

        assertNotNull(result);
        assertEquals(response, result);
        verify(saleDetailsPersistancePort).readById(1L);
    }

    @Test
    void testCreateEntity() {
        when(saleDetailsDomainMapper.saleDetailsRequestToSaleDetails(request)).thenReturn(saleDetails);
        when(saleDetailsPersistancePort.create(saleDetails)).thenReturn(saleDetails);
        when(saleDetailsDomainMapper.saleDetailsToSaleDetailsResponse(saleDetails)).thenReturn(response);

        SaleDetailsResponse result = saleDetailsService.createEntity(request);

        assertNotNull(result);
        assertEquals(response, result);
        verify(saleDetailsPersistancePort).create(saleDetails);
    }

    @Test
    void testUpdateEntity() {
        when(saleDetailsDomainMapper.saleDetailsRequestToSaleDetails(request)).thenReturn(saleDetails);
        when(saleDetailsPersistancePort.update(saleDetails, 1L)).thenReturn(saleDetails);
        when(saleDetailsDomainMapper.saleDetailsToSaleDetailsResponse(saleDetails)).thenReturn(response);

        SaleDetailsResponse result = saleDetailsService.updateEntity(request, 1L);

        assertNotNull(result);
        assertEquals(response, result);
        verify(saleDetailsPersistancePort).update(saleDetails, 1L);
    }

    @Test
    void testDeleteEntityById() {
        doNothing().when(saleDetailsPersistancePort).deleteById(1L);

        assertDoesNotThrow(() -> saleDetailsService.deleteEntityById(1L));
        verify(saleDetailsPersistancePort).deleteById(1L);
    }
}