package com.tienda.salieservice.infrastructure.rest.controller;


import com.tienda.salieservice.application.services.SaleService;
import com.tienda.salieservice.domain.model.dto.Sale;
import com.tienda.salieservice.domain.model.dto.request.SaleRequest;
import com.tienda.salieservice.domain.model.dto.response.SaleResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/sale")
@AllArgsConstructor
public class SaleController {

    private final SaleService saleService;

    /**
     * Retrieves a sale by its unique identifier (UUID)
     *
     * @param id the UUID of the sale to retrieve
     * @return the sale details as a response
     */
    @GetMapping("/{id}")
    public ResponseEntity<SaleResponse> getSaleById(@PathVariable UUID id) {
        var saleResponse = saleService.getById(id);
        return ResponseEntity.ok(saleResponse);
    }

    /**
     * Creates a new sale
     *
     * @param saleRequest the details of the sale to be created
     * @return the response containing the created sale details
     */

    @PostMapping
    public ResponseEntity<SaleResponse> createSale(@RequestBody @Valid SaleRequest saleRequest) {
        var saleResponse = saleService.createEntity(saleRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(saleResponse);
    }

    /**
     * Updates an existing sale by its identifier
     *
     * @param id the UUID of the sale to update
     * @param saleRequest the updated sale details
     * @return the response containing the updated sale details
     */
    @PutMapping("/{id}")
    public ResponseEntity<SaleResponse> updateSale(@PathVariable UUID id, @RequestBody @Valid SaleRequest saleRequest) {
        var saleResponse = saleService.updateEntity(saleRequest, id);
        return ResponseEntity.ok(saleResponse);
    }

    /**
     * Deletes a sale by its unique identifier
     *
     * @param id the UUID of the sale to delete
     * @return a response with no content after successful deletion
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSale(@PathVariable UUID id) {
        saleService.deleteEntityById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Retrieves sales that occurred between the specified start and end dates
     *
     * @param startDate the start date for filtering sales
     * @param endDate the end date for filtering sales
     * @return a list of sales that occurred between the provided dates
     */
    @GetMapping("/by-dates")
    public ResponseEntity<List<Sale>> getSalesBetweenDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        var sales = saleService.findAllSalesBetweenDates(startDate, endDate);
        return ResponseEntity.ok(sales);
    }

    /**
     * Retrieves sales that involve a specific product identified by its product ID
     *
     * @param productId the product ID for filtering sales
     * @return a list of sales related to the specified product
     */
    @GetMapping("/by-product/{productId}")
    public ResponseEntity<List<Sale>> getSalesByProductId(@PathVariable String productId) {
        var sales = saleService.findAllSalesByProductId(productId);
        return ResponseEntity.ok(sales);
    }

    /**
     * Retrieves sales that involve a specific product and occurred between the specified dates
     *
     * @param productId the product ID for filtering sales
     * @param startDate the start date for filtering sales
     * @param endDate the end date for filtering sales
     * @return a list of sales related to the specified product within the provided date range
     */
    @GetMapping("/by-product/{productId}/dates")
    public ResponseEntity<List<Sale>> getSalesByProductIdAndDates(
            @PathVariable String productId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        var sales = saleService.findAllSalesByProductId(productId, startDate, endDate);
        return ResponseEntity.ok(sales);
    }

    /**
     * Retrieves sales made by a specific customer
     *
     * @param customerName the name of the customer for filtering sales
     * @return a list of sales made by the specified customer
     */
    @GetMapping("/by-customer")
    public ResponseEntity<List<Sale>> getSalesByCustomer(@RequestParam String customerName) {
        var sales = saleService.findAllSalesByCustomer(customerName);
        return ResponseEntity.ok(sales);
    }

    /**
     * Retrieves sales made by a specific customer and occurred between the specified dates
     *
     * @param customerName the name of the customer for filtering sales
     * @param startDate the start date for filtering sales
     * @param endDate the end date for filtering sales
     * @return a list of sales made by the specified customer within the provided date range
     */
    @GetMapping("/by-customer/dates")
    public ResponseEntity<List<Sale>> getSalesByCustomerAndDates(
            @RequestParam String customerName,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        var sales = saleService.findAllSalesByCustomer(customerName, startDate, endDate);
        return ResponseEntity.ok(sales);
    }

    /**
     * Retrieves sales with a price greater than the specified amount
     *
     * @param price the price threshold for filtering sales
     * @return a list of sales with a price greater than the specified value
     */
    @GetMapping("/by-price/greater-than")
    public ResponseEntity<List<Sale>> getSalesGreaterThan(@RequestParam Double price) {
        var sales = saleService.findAllSalesGreatPriceThan(price);
        return ResponseEntity.ok(sales);
    }

    /**
     * Retrieves sales with a price greater less the specified amount
     * @param price the price threshold for filtering sales
     * @return a list of sales with a price greater than the specified value
     */
    @GetMapping("/by-price/less-than")
    public ResponseEntity<List<Sale>> getSalesLessThan(@RequestParam Double price) {
        var sales = saleService.findAllSalesGreatPriceLess(price);
        return ResponseEntity.ok(sales);
    }
}
