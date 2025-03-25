package com.tienda.salieservice.application.services;

import com.tienda.salieservice.application.mapper.SaleDomainMapper;
import com.tienda.salieservice.application.validator.FeignValidator;
import com.tienda.salieservice.domain.model.dto.Sale;
import com.tienda.salieservice.domain.model.dto.request.SaleRequest;
import com.tienda.salieservice.domain.model.dto.response.SaleResponse;
import com.tienda.salieservice.domain.port.SalePersistancePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SaleServiceTest {
    @Mock
    private SalePersistancePort salePersistancePort;

    @Mock
    private SaleDomainMapper saleDomainMapper;

    @Mock
    private FeignValidator feignValidator;

    @InjectMocks
    private SaleService saleService;

    private UUID saleId;
    private Sale sale;
    private SaleResponse saleResponse;
    private SaleRequest saleRequest;

    @BeforeEach
    void setUp() {
        saleId = UUID.randomUUID();
        sale = new Sale(saleId, null, BigDecimal.TEN, "John Doe", "Credit Card", BigDecimal.ONE, Collections.emptyList());
        saleResponse = new SaleResponse();
        saleRequest = new SaleRequest("John Doe", "Credit Card", Collections.emptyList());
    }

    @Test
    void testGetById() {
        when(salePersistancePort.readById(saleId)).thenReturn(sale);
        when(saleDomainMapper.saleToSaleResponse(sale)).thenReturn(saleResponse);

        SaleResponse result = saleService.getById(saleId);

        assertNotNull(result);
        verify(salePersistancePort).readById(saleId);
        verify(saleDomainMapper).saleToSaleResponse(sale);
    }

    @Test
    void testCreateEntity() {
        when(saleDomainMapper.saleRequestToSale(saleRequest)).thenReturn(sale);
        when(salePersistancePort.create(sale)).thenReturn(sale);
        when(saleDomainMapper.saleToSaleResponse(sale)).thenReturn(saleResponse);

        SaleResponse result = saleService.createEntity(saleRequest);

        assertNotNull(result);
        verify(saleDomainMapper).saleRequestToSale(saleRequest);
        verify(salePersistancePort).create(sale);
        verify(saleDomainMapper).saleToSaleResponse(sale);
    }

    @Test
    void testUpdateEntity() {
        when(saleDomainMapper.saleRequestToSale(saleRequest)).thenReturn(sale);
        when(salePersistancePort.update(sale, saleId)).thenReturn(sale);
        when(saleDomainMapper.saleToSaleResponse(sale)).thenReturn(saleResponse);

        SaleResponse result = saleService.updateEntity(saleRequest, saleId);

        assertNotNull(result);
        verify(saleDomainMapper).saleRequestToSale(saleRequest);
        verify(salePersistancePort).update(sale, saleId);
        verify(saleDomainMapper).saleToSaleResponse(sale);
    }

    @Test
    void testDeleteEntityById() {
        doNothing().when(salePersistancePort).deleteById(saleId);

        assertDoesNotThrow(() -> saleService.deleteEntityById(saleId));
        verify(salePersistancePort).deleteById(saleId);
    }

    @Test
    void testFindAllSalesBetweenDates() {
        LocalDate startDate = LocalDate.now().minusDays(7);
        LocalDate endDate = LocalDate.now();
        when(salePersistancePort.findAllSalesBetweenDates(startDate, endDate)).thenReturn(List.of(sale));

        List<Sale> result = saleService.findAllSalesBetweenDates(startDate, endDate);

        assertFalse(result.isEmpty());
        verify(salePersistancePort).findAllSalesBetweenDates(startDate, endDate);
    }
}