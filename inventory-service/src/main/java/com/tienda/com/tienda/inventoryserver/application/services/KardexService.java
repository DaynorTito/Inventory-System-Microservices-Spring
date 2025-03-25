package com.tienda.com.tienda.inventoryserver.application.services;

import com.tienda.com.tienda.inventoryserver.application.mapper.KardexDomainMapper;
import com.tienda.com.tienda.inventoryserver.application.validation.FeignValidator;
import com.tienda.com.tienda.inventoryserver.domain.model.dto.Kardex;
import com.tienda.com.tienda.inventoryserver.domain.model.dto.Product;
import com.tienda.com.tienda.inventoryserver.domain.model.dto.TopSellingProduct;
import com.tienda.com.tienda.inventoryserver.domain.model.dto.request.KardexRequest;
import com.tienda.com.tienda.inventoryserver.domain.model.dto.response.KardexResponse;
import com.tienda.com.tienda.inventoryserver.domain.port.KardexPersistancePort;
import com.tienda.com.tienda.inventoryserver.application.useCases.KardexUseCases;
import com.tienda.com.tienda.inventoryserver.infraestructure.adapters.client.ProductFeignClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class KardexService implements KardexUseCases {

    private final KardexPersistancePort kardexPersistancePort;
    private final KardexDomainMapper kardexDomainMapper;
    private final ProductFeignClient productFeignClient;
    private final FeignValidator feignValidator;


    /**
     * Retrieves the history of movements for a given product
     *
     * @param productId The ID of the product
     * @return A list of KardexResponse containing product movement history
     */
    @Override
    public List<KardexResponse> getProductHistory(String productId) {
        var kardexList = kardexPersistancePort.findAllKardexByProductId(productId);
        return kardexList.stream()
                .map(kardexDomainMapper::kardexToKardexResponse)
                .toList();
    }

    /**
     * Retrieves inventory movements within a specified date range
     *
     * @param startDate The start date
     * @param endDate The end date
     * @return A list of KardexResponse containing inventory movements
     */
    @Override
    public List<KardexResponse> getInventoryMovements(LocalDate startDate, LocalDate endDate) {
        var kardexList = kardexPersistancePort.findAllKardexByMovementDateBetween(startDate, endDate);
        return kardexList.stream()
                .map(kardexDomainMapper::kardexToKardexResponse)
                .toList();
    }

    /**
     * Retrieves inventory movements for a specific product within a given date range
     *
     * @param startDate The start date
     * @param endDate The end date
     * @param productId The ID of the product
     * @return A list of KardexResponse containing inventory movements
     */
    @Override
    public List<KardexResponse> getInventoryMovements(LocalDate startDate, LocalDate endDate, String productId) {
        var kardexList = kardexPersistancePort.findAllKardexByMovementDateBetweenAndProductId(
                startDate, endDate, productId);
        return kardexList.stream()
                .map(kardexDomainMapper::kardexToKardexResponse)
                .toList();
    }

    /**
     * Retrieves a report of the most sold products within a specified date range
     *
     * @param startDate The start date
     * @param endDate The end date
     * @param limit The maximum number of products to retrieve
     * @return A list of Products representing the top-selling items
     */
    @Override
    public List<Product> getMostSoldProductsReport(LocalDate startDate, LocalDate endDate, Integer limit) {
        return getMostSoldProductsReport(limit, startDate, endDate);
    }

    /**
     * Retrieves a report of the most sold products within the last month
     *
     * @param limit The maximum number of products to retrieve
     * @return A list of Products representing the top-selling items
     */
    @Override
    public List<Product> getMostSoldProductsReport(Integer limit) {
        var endDate = LocalDate.now();
        var startDate = endDate.minusMonths(1);
        return getMostSoldProductsReport(limit, startDate, endDate);
    }

    /**
     * Retrieves a Kardex entry by its unique identifier
     *
     * @param uuid The unique identifier of the Kardex entry
     * @return The corresponding KardexResponse
     */
    @Override
    public KardexResponse getById(UUID uuid) {
        Kardex kardex = kardexPersistancePort.readById(uuid);
        return kardexDomainMapper.kardexToKardexResponse(kardex);
    }

    /**
     * Creates a new Kardex entry
     *
     * @param request The KardexRequest containing movement details
     * @return The created KardexResponse
     */
    @Override
    public KardexResponse createEntity(KardexRequest request) {
        validateKardexRequest(request);
        var kardex = kardexDomainMapper.kardexRequestToKardex(request);
        if (request.getMovementDate() == null)
            kardex.setMovementDate(LocalDate.now());
        kardex.setTotalPrice(request.getUnitPrice().multiply(new BigDecimal(request.getQuantity())));
        var savedKardex = kardexPersistancePort.create(kardex);
        return kardexDomainMapper.kardexToKardexResponse(savedKardex);
    }

    /**
     * Updates an existing Kardex entry
     *
     * @param request The KardexRequest containing updated details
     * @param uuid The unique identifier of the Kardex entry to update
     * @return The updated KardexResponse
     */
    @Override
    public KardexResponse updateEntity(KardexRequest request, UUID uuid) {
        if (request.getProductId() != null) validateKardexRequest(request);
        var existinKardex = kardexPersistancePort.readById(uuid);
        var kardex = kardexDomainMapper.kardexRequestToKardex(request);
        kardex.setId(uuid);
        if (request.getUnitPrice() != null || request.getQuantity() != null) {
            if (request.getQuantity() == null)
                kardex.setTotalPrice(request.getUnitPrice().multiply(new BigDecimal(existinKardex.getQuantity())));
            else
                kardex.setTotalPrice(existinKardex.getUnitPrice().multiply(new BigDecimal(request.getQuantity())));
        }
        var updatedKardex = kardexPersistancePort.update(kardex, uuid);
        return kardexDomainMapper.kardexToKardexResponse(updatedKardex);
    }

    /**
     * Deletes a Kardex entry by its unique identifier
     *
     * @param uuid The unique identifier of the Kardex entry to delete
     */
    @Override
    public void deleteEntityById(UUID uuid) {
        var kardexToDelete = kardexPersistancePort.readById(uuid);
        kardexPersistancePort.deleteById(uuid);
    }

    /**\
     * Retrieves a report of the most sold products within a specified date range
     *
     * @param limit The maximum number of products to retrieve
     * @param startDate The start date
     * @param endDate The end date
     * @return A list of Products representing the top-selling items
     */
    private List<Product> getMostSoldProductsReport(Integer limit, LocalDate startDate, LocalDate endDate) {
        List<TopSellingProduct> topSelling = kardexPersistancePort.findTopSellingProducts(startDate, endDate, limit);
        return topSelling.stream()
                .map(kardex -> productFeignClient.getProductByCod(kardex.getProductId()))
                .toList();
    }

    /**
     * Validates the existence of a product in the Kardex request
     *
     * @param request The KardexRequest to validate
     */
    private void validateKardexRequest(KardexRequest request) {
        feignValidator.verifyExistingProduct(request.getProductId());
    }
}
