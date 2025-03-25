package com.tienda.com.tienda.inventoryserver.application.services;

import com.tienda.com.tienda.inventoryserver.application.mapper.KardexDomainMapper;
import com.tienda.com.tienda.inventoryserver.application.validation.FeignValidator;
import com.tienda.com.tienda.inventoryserver.domain.model.constant.TypeMove;
import com.tienda.com.tienda.inventoryserver.domain.model.dto.Kardex;
import com.tienda.com.tienda.inventoryserver.domain.model.dto.Product;
import com.tienda.com.tienda.inventoryserver.domain.model.dto.TopSellingProduct;
import com.tienda.com.tienda.inventoryserver.domain.model.dto.request.KardexRequest;
import com.tienda.com.tienda.inventoryserver.domain.model.dto.response.KardexResponse;
import com.tienda.com.tienda.inventoryserver.domain.port.KardexPersistancePort;
import com.tienda.com.tienda.inventoryserver.infraestructure.adapters.client.ProductFeignClient;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KardexServiceTest {
    @Mock
    private KardexPersistancePort kardexPersistancePort;

    @Mock
    private KardexDomainMapper kardexDomainMapper;

    @Mock
    private ProductFeignClient productFeignClient;

    @Mock
    private FeignValidator feignValidator;

    @InjectMocks
    private KardexService kardexService;

    private Kardex kardex;
    private KardexResponse kardexResponse;
    private KardexRequest kardexRequest;
    private Product product;
    private String productId;
    private UUID kardexId;
    private LocalDate startDate;
    private LocalDate endDate;

    @BeforeEach
    void setUp() {
        productId = "PROD-001";
        kardexId = UUID.randomUUID();
        startDate = LocalDate.now().minusDays(30);
        endDate = LocalDate.now();

        product = new Product();
        product.setCod(productId);
        product.setName("Test Product");

        kardex = new Kardex();
        kardex.setId(kardexId);
        kardex.setProductId(productId);
        kardex.setQuantity(10);
        kardex.setUnitPrice(new BigDecimal("15.50"));
        kardex.setTotalPrice(new BigDecimal("155.00"));
        kardex.setTypeMovement(TypeMove.INCOME);
        kardex.setMovementDate(LocalDate.now());

        kardexResponse = new KardexResponse();
        kardexResponse.setId(kardexId);
        kardexResponse.setProductId(product);
        kardexResponse.setQuantity(10);
        kardexResponse.setUnitPrice(new BigDecimal("15.50"));
        kardexResponse.setTotalPrice(new BigDecimal("155.00"));
        kardexResponse.setTypeMovement(TypeMove.INCOME);
        kardexResponse.setMovementDate(LocalDate.now());

        kardexRequest = new KardexRequest();
        kardexRequest.setProductId(productId);
        kardexRequest.setQuantity(10);
        kardexRequest.setUnitPrice(new BigDecimal("15.50"));
        kardexRequest.setTypeMovement(TypeMove.INCOME);
        kardexRequest.setMovementDate(LocalDate.now());
    }

    @Test
    void getProductHistory_ShouldReturnKardexResponseList() {
        List<Kardex> kardexList = Collections.singletonList(kardex);
        when(kardexPersistancePort.findAllKardexByProductId(productId)).thenReturn(kardexList);
        when(kardexDomainMapper.kardexToKardexResponse(kardex)).thenReturn(kardexResponse);
        List<KardexResponse> result = kardexService.getProductHistory(productId);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(kardexResponse, result.getFirst());
        verify(kardexPersistancePort).findAllKardexByProductId(productId);
        verify(kardexDomainMapper).kardexToKardexResponse(kardex);
    }

    @Test
    void getInventoryMovements_WithDateRange_ShouldReturnKardexResponseList() {
        List<Kardex> kardexList = Collections.singletonList(kardex);
        when(kardexPersistancePort.findAllKardexByMovementDateBetween(startDate, endDate)).thenReturn(kardexList);
        when(kardexDomainMapper.kardexToKardexResponse(kardex)).thenReturn(kardexResponse);
        List<KardexResponse> result = kardexService.getInventoryMovements(startDate, endDate);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(kardexResponse, result.getFirst());
        verify(kardexPersistancePort).findAllKardexByMovementDateBetween(startDate, endDate);
        verify(kardexDomainMapper).kardexToKardexResponse(kardex);
    }

    @Test
    void getInventoryMovements_WithDateRangeAndProductId_ShouldReturnKardexResponseList() {
        List<Kardex> kardexList = Collections.singletonList(kardex);
        when(kardexPersistancePort.findAllKardexByMovementDateBetweenAndProductId(startDate, endDate, productId))
                .thenReturn(kardexList);
        when(kardexDomainMapper.kardexToKardexResponse(kardex)).thenReturn(kardexResponse);
        List<KardexResponse> result = kardexService.getInventoryMovements(startDate, endDate, productId);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(kardexResponse, result.getFirst());
        verify(kardexPersistancePort).findAllKardexByMovementDateBetweenAndProductId(startDate, endDate, productId);
        verify(kardexDomainMapper).kardexToKardexResponse(kardex);
    }

    @Test
    void getMostSoldProductsReport_WithDateRangeAndLimit_ShouldReturnProductList() {
        TopSellingProduct topSellingProduct = new TopSellingProduct() {
            @Override
            public String getProductId() {
                return productId;
            }
        };
        List<TopSellingProduct> topSellingProducts = List.of(topSellingProduct);
        when(kardexPersistancePort.findTopSellingProducts(startDate, endDate, 5)).thenReturn(topSellingProducts);
        when(productFeignClient.getProductByCod(productId)).thenReturn(product);
        List<Product> result = kardexService.getMostSoldProductsReport(startDate, endDate, 5);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(product, result.getFirst());
        verify(kardexPersistancePort).findTopSellingProducts(startDate, endDate, 5);
        verify(productFeignClient).getProductByCod(productId);
    }

    @Test
    void getMostSoldProductsReport_WithLimit_ShouldReturnProductList() {
        TopSellingProduct topSellingProduct = new TopSellingProduct() {
            @Override
            public String getProductId() {
                return productId;
            }
        };
        List<TopSellingProduct> topSellingProducts = List.of(topSellingProduct);
        when(kardexPersistancePort.findTopSellingProducts(any(LocalDate.class), any(LocalDate.class), eq(5)))
                .thenReturn(topSellingProducts);
        when(productFeignClient.getProductByCod(productId)).thenReturn(product);
        List<Product> result = kardexService.getMostSoldProductsReport(5);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(product, result.getFirst());
        verify(kardexPersistancePort).findTopSellingProducts(any(LocalDate.class), any(LocalDate.class), eq(5));
        verify(productFeignClient).getProductByCod(productId);
    }

    @Test
    void getById_ShouldReturnKardexResponse() {
        when(kardexPersistancePort.readById(kardexId)).thenReturn(kardex);
        when(kardexDomainMapper.kardexToKardexResponse(kardex)).thenReturn(kardexResponse);
        KardexResponse result = kardexService.getById(kardexId);
        assertNotNull(result);
        assertEquals(kardexResponse, result);
        verify(kardexPersistancePort).readById(kardexId);
        verify(kardexDomainMapper).kardexToKardexResponse(kardex);
    }

    @Test
    void createEntity_WithValidRequest_ShouldReturnKardexResponse() {
        when(kardexDomainMapper.kardexRequestToKardex(kardexRequest)).thenReturn(kardex);
        when(kardexPersistancePort.create(kardex)).thenReturn(kardex);
        when(kardexDomainMapper.kardexToKardexResponse(kardex)).thenReturn(kardexResponse);
        KardexResponse result = kardexService.createEntity(kardexRequest);
        assertNotNull(result);
        assertEquals(kardexResponse, result);
        verify(feignValidator).verifyExistingProduct(productId);
        verify(kardexDomainMapper).kardexRequestToKardex(kardexRequest);
        verify(kardexPersistancePort).create(kardex);
        verify(kardexDomainMapper).kardexToKardexResponse(kardex);
    }

    @Test
    void createEntity_WithNullMovementDate_ShouldSetCurrentDate() {
        kardexRequest.setMovementDate(null);
        when(kardexDomainMapper.kardexRequestToKardex(kardexRequest)).thenReturn(kardex);
        when(kardexPersistancePort.create(kardex)).thenReturn(kardex);
        when(kardexDomainMapper.kardexToKardexResponse(kardex)).thenReturn(kardexResponse);
        kardexService.createEntity(kardexRequest);
        verify(kardexDomainMapper).kardexRequestToKardex(kardexRequest);
        assertEquals(LocalDate.now().getYear(), kardex.getMovementDate().getYear());
        assertEquals(LocalDate.now().getMonth(), kardex.getMovementDate().getMonth());
        assertEquals(LocalDate.now().getDayOfMonth(), kardex.getMovementDate().getDayOfMonth());
    }

    @Test
    void createEntity_ShouldCalculateTotalPrice() {
        when(kardexDomainMapper.kardexRequestToKardex(kardexRequest)).thenReturn(kardex);
        when(kardexPersistancePort.create(kardex)).thenReturn(kardex);
        when(kardexDomainMapper.kardexToKardexResponse(kardex)).thenReturn(kardexResponse);
        kardexService.createEntity(kardexRequest);
        verify(kardexDomainMapper).kardexRequestToKardex(kardexRequest);
        assertEquals(new BigDecimal("155.00"), kardex.getTotalPrice());
    }

    @Test
    void updateEntity_WithValidRequest_ShouldReturnUpdatedKardexResponse() {
        Kardex existingKardex = new Kardex();
        existingKardex.setId(kardexId);
        existingKardex.setProductId(productId);
        existingKardex.setQuantity(5);
        existingKardex.setUnitPrice(new BigDecimal("10.00"));
        existingKardex.setTotalPrice(new BigDecimal("50.00"));
        when(kardexPersistancePort.readById(kardexId)).thenReturn(existingKardex);
        when(kardexDomainMapper.kardexRequestToKardex(kardexRequest)).thenReturn(kardex);
        when(kardexPersistancePort.update(kardex, kardexId)).thenReturn(kardex);
        when(kardexDomainMapper.kardexToKardexResponse(kardex)).thenReturn(kardexResponse);
        KardexResponse result = kardexService.updateEntity(kardexRequest, kardexId);
        assertNotNull(result);
        assertEquals(kardexResponse, result);
        verify(feignValidator).verifyExistingProduct(productId);
        verify(kardexDomainMapper).kardexRequestToKardex(kardexRequest);
        verify(kardexPersistancePort).update(kardex, kardexId);
        verify(kardexDomainMapper).kardexToKardexResponse(kardex);
    }

    @Test
    void updateEntity_WithNullProductId_ShouldNotValidateProduct() {
        kardexRequest.setProductId(null);
        Kardex existingKardex = new Kardex();
        existingKardex.setId(kardexId);
        existingKardex.setProductId(productId);
        existingKardex.setQuantity(5);
        existingKardex.setUnitPrice(new BigDecimal("10.00"));
        existingKardex.setTotalPrice(new BigDecimal("50.00"));
        when(kardexPersistancePort.readById(kardexId)).thenReturn(existingKardex);
        when(kardexDomainMapper.kardexRequestToKardex(kardexRequest)).thenReturn(kardex);
        when(kardexPersistancePort.update(kardex, kardexId)).thenReturn(kardex);
        when(kardexDomainMapper.kardexToKardexResponse(kardex)).thenReturn(kardexResponse);
        kardexService.updateEntity(kardexRequest, kardexId);
        verify(feignValidator, never()).verifyExistingProduct(anyString());
    }

    @Test
    void updateEntity_WithQuantityUpdate_ShouldCalculateTotalPrice() {
        kardexRequest.setUnitPrice(null);
        kardexRequest.setQuantity(20);
        Kardex existingKardex = new Kardex();
        existingKardex.setId(kardexId);
        existingKardex.setProductId(productId);
        existingKardex.setQuantity(5);
        existingKardex.setUnitPrice(new BigDecimal("10.00"));
        existingKardex.setTotalPrice(new BigDecimal("50.00"));
        when(kardexPersistancePort.readById(kardexId)).thenReturn(existingKardex);
        when(kardexDomainMapper.kardexRequestToKardex(kardexRequest)).thenReturn(kardex);
        when(kardexPersistancePort.update(kardex, kardexId)).thenReturn(kardex);
        when(kardexDomainMapper.kardexToKardexResponse(kardex)).thenReturn(kardexResponse);
        kardexService.updateEntity(kardexRequest, kardexId);
        verify(kardexPersistancePort).update(kardex, kardexId);
        assertEquals(new BigDecimal("200.00"), kardex.getTotalPrice());
    }

    @Test
    void updateEntity_WithUnitPriceUpdate_ShouldCalculateTotalPrice() {
        kardexRequest.setUnitPrice(new BigDecimal("20.00"));
        kardexRequest.setQuantity(null);
        Kardex existingKardex = new Kardex();
        existingKardex.setId(kardexId);
        existingKardex.setProductId(productId);
        existingKardex.setQuantity(5);
        existingKardex.setUnitPrice(new BigDecimal("10.00"));
        existingKardex.setTotalPrice(new BigDecimal("50.00"));
        when(kardexPersistancePort.readById(kardexId)).thenReturn(existingKardex);
        when(kardexDomainMapper.kardexRequestToKardex(kardexRequest)).thenReturn(kardex);
        when(kardexPersistancePort.update(kardex, kardexId)).thenReturn(kardex);
        when(kardexDomainMapper.kardexToKardexResponse(kardex)).thenReturn(kardexResponse);
        kardexService.updateEntity(kardexRequest, kardexId);
        verify(kardexPersistancePort).update(kardex, kardexId);
        assertEquals(new BigDecimal("100.00"), kardex.getTotalPrice());
    }

    @Test
    void deleteEntityById_ShouldDeleteKardex() {
        when(kardexPersistancePort.readById(kardexId)).thenReturn(kardex);
        kardexService.deleteEntityById(kardexId);
        verify(kardexPersistancePort).readById(kardexId);
        verify(kardexPersistancePort).deleteById(kardexId);
    }
}