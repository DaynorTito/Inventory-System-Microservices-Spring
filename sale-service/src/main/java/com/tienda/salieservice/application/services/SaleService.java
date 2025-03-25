package com.tienda.salieservice.application.services;

import com.tienda.salieservice.application.mapper.SaleDomainMapper;
import com.tienda.salieservice.application.validator.FeignValidator;
import com.tienda.salieservice.domain.model.dto.Sale;
import com.tienda.salieservice.domain.model.dto.request.SaleInventoryRequest;
import com.tienda.salieservice.domain.model.dto.request.SaleRequest;
import com.tienda.salieservice.domain.model.dto.response.SaleResponse;
import com.tienda.salieservice.domain.port.SalePersistancePort;
import com.tienda.salieservice.application.useCases.SaleUseCases;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class SaleService implements SaleUseCases {

    private final SalePersistancePort salePersistancePort;
    private final SaleDomainMapper saleDomainMapper;
    private final FeignValidator feignValidator;

    /**
     * Retrieves all sales that occurred between the specified start and end dates
     *
     * @param startDate The start date of the range
     * @param endDate The end date of the range
     * @return A list of `Sale` objects representing sales within the specified date range
     */
    @Override
    public List<Sale> findAllSalesBetweenDates(LocalDate startDate, LocalDate endDate) {
        return salePersistancePort.findAllSalesBetweenDates(startDate, endDate);
    }

    /**
     * Retrieves all sales related to a specific product by its product ID
     *
     * @param productId The ID of the product
     * @return A list of `Sale` objects related to the given product ID
     */
    @Override
    public List<Sale> findAllSalesByProductId(String productId) {
        return salePersistancePort.findAllSalesByProductId(productId);
    }

    /**
     * Retrieves all sales related to a specific product and within a specified date range
     *
     * @param productId The ID of the product
     * @param startDate The start date of the range
     * @param endDate The end date of the range
     * @return A list of `Sale` objects related to the given product and date range
     */
    @Override
    public List<Sale> findAllSalesByProductId(String productId, LocalDate startDate, LocalDate endDate) {
        return salePersistancePort.findAllSalesByProductId(productId, startDate, endDate);
    }

    /**
     * Retrieves all sales made by a specific customer
     *
     * @param customerName The name of the customer
     * @return A list of `Sale` objects made by the specified customer
     */
    @Override
    public List<Sale> findAllSalesByCustomer(String customerName) {
        return salePersistancePort.findAllSalesByCustomer(customerName);
    }

    /**
     * Retrieves all sales made by a specific customer within a specified date range
     *
     * @param customerName The name of the customer
     * @param startDate The start date of the range
     * @param endDate The end date of the range
     * @return A list of `Sale` objects made by the specified customer and within the date range
     */
    @Override
    public List<Sale> findAllSalesByCustomer(String customerName, LocalDate startDate, LocalDate endDate) {
        return salePersistancePort.findAllSalesByCustomer(customerName, startDate, endDate);
    }

    /**
     * Retrieves all sales with a price greater than the specified price
     *
     * @param price The price to compare against
     * @return A list of `Sale` objects with a price greater than the specified value
     */
    @Override
    public List<Sale> findAllSalesGreatPriceThan(Double price) {
        return salePersistancePort.findAllSalesGreatPriceThan(BigDecimal.valueOf(price));
    }

    /**
     * Retrieves all sales with a price less than the specified price
     *
     * @param price The price to compare against
     * @return A list of `Sale` objects with a price less than the specified value
     */
    @Override
    public List<Sale> findAllSalesGreatPriceLess(Double price) {
        return salePersistancePort.findAllSalesGreatPriceLess(BigDecimal.valueOf(price));
    }

    /**
     * Retrieves a sale by its unique identifier
     *
     * @param uuid The unique identifier of the sale
     * @return A `SaleResponse` object containing the details of the sale
     */
    @Override
    public SaleResponse getById(UUID uuid) {
        Sale sale = salePersistancePort.readById(uuid);
        return saleDomainMapper.saleToSaleResponse(sale);
    }

    /**
     * Creates a new sale entity
     *
     * @param request A `SaleRequest` object containing the information to create the sale
     * @return A `SaleResponse` object containing the details of the newly created sale
     */
    @Override
    public SaleResponse createEntity(SaleRequest request) {
        Sale saleDomain = saleDomainMapper.saleRequestToSale(request);
        registerSaleInventoryService(saleDomain);
        Sale createdSale = salePersistancePort.create(saleDomain);
        return saleDomainMapper.saleToSaleResponse(createdSale);
    }

    /**
     * Registers the sale details in the inventory service
     *
     * @param createdSale The sale entity to register in the inventory service
     */
    public void registerSaleInventoryService(Sale createdSale) {
        createdSale.getSaleDetails().forEach(saleDetails ->
                feignValidator.registerSale(SaleInventoryRequest.builder()
                        .unitPrice(saleDetails.getUnitPrice())
                        .quantity(saleDetails.getQuantity())
                        .productId(saleDetails.getProductId()).build()));
    }

    /**
     * Updates an existing sale entity
     *
     * @param request A `SaleRequest` object containing the updated information for the sale
     * @param uuid The unique identifier of the sale to update
     * @return A `SaleResponse` object containing the updated details of the sale
     */
    @Override
    public SaleResponse updateEntity(SaleRequest request, UUID uuid) {
        Sale saleDomain = saleDomainMapper.saleRequestToSale(request);
        Sale updatedSale = salePersistancePort.update(saleDomain, uuid);
        return saleDomainMapper.saleToSaleResponse(updatedSale);
    }

    /**
     * Deletes a sale entity by its unique identifier
     *
     * @param uuid The unique identifier of the sale to delete
     */
    @Override
    public void deleteEntityById(UUID uuid) {
        salePersistancePort.deleteById(uuid);
    }
}
