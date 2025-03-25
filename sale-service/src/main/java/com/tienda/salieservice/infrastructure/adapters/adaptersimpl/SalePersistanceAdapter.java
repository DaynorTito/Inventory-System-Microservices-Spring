package com.tienda.salieservice.infrastructure.adapters.adaptersimpl;

import com.tienda.salieservice.domain.model.dto.Sale;
import com.tienda.salieservice.domain.port.SalePersistancePort;
import com.tienda.salieservice.infrastructure.adapters.entity.SaleDetailsEntity;
import com.tienda.salieservice.infrastructure.adapters.entity.SaleEntity;
import com.tienda.salieservice.infrastructure.adapters.exception.SaleNotFOund;
import com.tienda.salieservice.infrastructure.adapters.mapper.SaleMapper;
import com.tienda.salieservice.infrastructure.adapters.repository.SaleRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Transactional
@Service
@AllArgsConstructor
public class SalePersistanceAdapter implements SalePersistancePort {

    private final SaleRepository repository;
    private final SaleMapper mapper;

    /**
     * Finds all sales that occurred between the specified start and end dates
     *
     * @param startDate the start date of the sales period
     * @param endDate the end date of the sales period
     * @return a list of Sale objects within the specified date range
     */
    @Override
    public List<Sale> findAllSalesBetweenDates(LocalDate startDate, LocalDate endDate) {
        var startDateTime = LocalDateTime.of(startDate, LocalTime.MIN);
        var endDateTime = LocalDateTime.of(endDate, LocalTime.MAX);
        var response = repository.findBySaleDateBetween(startDateTime, endDateTime);
        return response.stream().map(mapper::toDomain).toList();
    }

    /**
     * Finds all sales for a given product ID
     *
     * @param productId the ID of the product
     * @return a list of Sale objects for the specified product
     */
    @Override
    public List<Sale> findAllSalesByProductId(String productId) {
        var response = repository.findBySaleDetails_ProductId(productId);
        return response.stream().map(mapper::toDomain).toList();
    }

    /**
     * Finds all sales for a given product ID that occurred between the specified start and end dates
     *
     * @param productId the ID of the product
     * @param startDate the start date of the sales period
     * @param endDate the end date of the sales period
     * @return a list of Sale objects for the specified product within the date range
     */
    @Override
    public List<Sale> findAllSalesByProductId(String productId,
                                              LocalDate startDate, LocalDate endDate) {
        var startDateTime = LocalDateTime.of(startDate, LocalTime.MIN);
        var endDateTime = LocalDateTime.of(endDate, LocalTime.MAX);
        var response = repository.findBySaleDetails_ProductIdAndSaleDateBetween(
                productId, startDateTime, endDateTime);
        return response.stream().map(mapper::toDomain).toList();
    }

    /**
     * Finds all sales made by a customer with a specified name
     *
     * @param customerName the name of the customer
     * @return a list of Sale objects made by the customer
     */
    @Override
    public List<Sale> findAllSalesByCustomer(String customerName) {
        var response = repository.findByCustomerNameContainingIgnoreCase(customerName);
        return response.stream().map(mapper::toDomain).toList();
    }


    /**
     * Finds all sales made by a customer with a specified name that occurred between the given start and end dates
     *
     * @param customerName the name of the customer
     * @param startDate the start date of the sales period
     * @param endDate the end date of the sales period
     * @return a list of Sale objects made by the customer within the date range
     */
    @Override
    public List<Sale> findAllSalesByCustomer(String customerName, LocalDate startDate, LocalDate endDate) {
        var startDateTime = LocalDateTime.of(startDate, LocalTime.MIN);
        var endDateTime = LocalDateTime.of(endDate, LocalTime.MAX);
        var response = repository.findByCustomerNameContainingIgnoreCaseAndSaleDateBetween(
                customerName, startDateTime, endDateTime);
        return response.stream().map(mapper::toDomain).toList();
    }

    /**
     * Finds all sales with a total amount greater than or equal to the specified price
     *
     * @param price the minimum price for the sales
     * @return a list of Sale objects with a total amount greater than or equal to the price
     */
    @Override
    public List<Sale> findAllSalesGreatPriceThan(BigDecimal price) {
        var response = repository.findByTotalAmountGreaterThanEqual(price);
        return response.stream().map(mapper::toDomain).toList();
    }

    /**
     * Finds all sales with a total amount less than or equal to the specified price
     *
     * @param price the maximum price for the sales
     * @return a list of Sale objects with a total amount less than or equal to the price
     */
    @Override
    public List<Sale> findAllSalesGreatPriceLess(BigDecimal price) {
        var response = repository.findByTotalAmountLessThanEqual(price);
        return response.stream().map(mapper::toDomain).toList();
    }

    /**
     * Finds a sale by its ID
     *
     * @param uuid the ID of the sale
     * @return the Sale object if found
     * @throws SaleNotFOund if the sale is not found
     */
    @Override
    public Sale readById(UUID uuid) {
        var response = repository.findById(uuid).orElseThrow(
                () -> new SaleNotFOund("Venta no encontrada"));
        return mapper.toDomain(response);
    }

    /**
     * Creates a new sale
     *
     * @param sale the Sale object to be created
     * @return the created Sale object
     */
    @Override
    public Sale create(Sale sale) {
        SaleEntity saleEntity = mapper.toEntity(sale);
        if (saleEntity.getSaleDetails() != null) {
            for (SaleDetailsEntity detail : saleEntity.getSaleDetails()) {
                detail.setSale(saleEntity);
            }
        }
        SaleEntity savedEntity = repository.save(saleEntity);
        return mapper.toDomain(savedEntity);
    }

    /**
     * Updates an existing sale with the provided details
     *
     * @param request the Sale object with updated data
     * @param uuid the ID of the sale to be updated
     * @return the updated Sale object
     * @throws SaleNotFOund if the sale is not found
     */
    @Override
    public Sale update(Sale request, UUID uuid) {
        return repository.findById(uuid)
                .map(existingEntity -> {
                    request.setId(uuid);
                    var updated = repository.save(mapper.toEntity(request));
                    return mapper.toDomain(updated);
                })
                .orElseThrow(() -> new SaleNotFOund("Venta no encontrada"));
    }

    /**
     * Deletes a sale by its ID
     *
     * @param uuid the ID of the sale to be deleted
     * @throws SaleNotFOund if the sale is not found
     */
    @Override
    public void deleteById(UUID uuid) {
        repository.findById(uuid).orElseThrow(
                () -> new SaleNotFOund("Venta no encontrada"));
        repository.deleteById(uuid);
    }
}
