package com.tienda.com.tienda.inventoryserver.application.services;

import com.tienda.com.tienda.inventoryserver.application.mapper.KardexDomainMapper;
import com.tienda.com.tienda.inventoryserver.application.mapper.StockDomainMapper;
import com.tienda.com.tienda.inventoryserver.domain.model.dto.request.KardexRequest;
import com.tienda.com.tienda.inventoryserver.domain.model.dto.request.PurchaseInventoryRequest;
import com.tienda.com.tienda.inventoryserver.domain.model.dto.request.SaleInventoryRequest;
import com.tienda.com.tienda.inventoryserver.domain.model.dto.request.StockRequest;
import com.tienda.com.tienda.inventoryserver.domain.model.dto.response.KardexResponse;
import com.tienda.com.tienda.inventoryserver.domain.model.dto.response.StockResponse;
import com.tienda.com.tienda.inventoryserver.infraestructure.adapters.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ManagementInventoryTest {
    @Mock
    private KardexService kardexService;

    @Mock
    private StockService stockService;

    @Mock
    private StockDomainMapper stockMapper;

    @Mock
    private KardexDomainMapper kardexMapper;

    @InjectMocks
    private ManagementInventory managementInventory;

    private PurchaseInventoryRequest purchaseRequest;
    private SaleInventoryRequest saleRequest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        purchaseRequest = new PurchaseInventoryRequest(
                10,
                new BigDecimal("100.00"),
                UUID.randomUUID(),
                "product-123",
                LocalDate.now().plusDays(5)
        );

        saleRequest = new SaleInventoryRequest(
                5,
                "product-123",
                new BigDecimal("120.00")
        );

        assertEquals("product-123", saleRequest.getProductId());
        when(stockService.createEntity(any(StockRequest.class))).thenReturn(new StockResponse());
        when(kardexService.createEntity(any(KardexRequest.class))).thenReturn(new KardexResponse());
    }

    @Test
    public void registerInputInventory_ShouldReturnSuccessMessage_WhenValidRequest() {
        var stockRequest = mock(StockRequest.class);
        var kardexRequest = mock(KardexRequest.class);
        when(stockMapper.registerPurchase(purchaseRequest)).thenReturn(stockRequest);
        when(kardexMapper.createPurchaseKardex(purchaseRequest)).thenReturn(kardexRequest);
        String result = managementInventory.registerInputInventory(purchaseRequest);
        assertEquals("Registro de compra creado exitosamente", result);
        verify(stockService).createEntity(stockRequest);
        verify(kardexService).createEntity(kardexRequest);
    }

    @Test
    public void registerOutputInventory_ShouldReturnSuccessMessage_WhenValidRequest() {
        var kardexRequest = mock(KardexRequest.class);
        when(kardexMapper.createSaleKardex(saleRequest)).thenReturn(kardexRequest);
        assertEquals("product-123", saleRequest.getProductId());
        StockResponse mockStockResponse = mock(StockResponse.class);
        when(stockService.decrementQuantity(eq("product-123"), eq(5), any(BigDecimal.class)))
                .thenReturn(mockStockResponse);
        String result = managementInventory.registerOutputInventory(saleRequest);
        assertEquals("Registro de compra creado exitosamente", result);
        verify(kardexService).createEntity(kardexRequest);
    }

    @Test
    public void registerInputInventory_ShouldThrowException_WhenInvalidDateRange() {
        purchaseRequest.setExpiryDate(LocalDate.now().minusDays(1));
        ValidationException thrown = assertThrows(ValidationException.class,
                () -> managementInventory.registerInputInventory(purchaseRequest));
        assertEquals("La fecha de vencimiento esta demasiado proxima, minimo 3 dias de diferencia", thrown.getMessage());
    }
}
