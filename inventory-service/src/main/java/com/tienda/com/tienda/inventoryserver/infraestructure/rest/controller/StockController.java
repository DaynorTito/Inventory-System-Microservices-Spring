package com.tienda.com.tienda.inventoryserver.infraestructure.rest.controller;


import com.tienda.com.tienda.inventoryserver.application.services.StockService;
import com.tienda.com.tienda.inventoryserver.domain.model.constant.StockStatus;
import com.tienda.com.tienda.inventoryserver.domain.model.dto.Product;
import com.tienda.com.tienda.inventoryserver.domain.model.dto.request.StockRequest;
import com.tienda.com.tienda.inventoryserver.domain.model.dto.response.StockResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/stock")
@AllArgsConstructor
public class StockController {

    private final StockService stockService;

    /**
     * Retrieves the complete list of stock items in the system
     *
     * @return A list of stock responses
     */
    @GetMapping
    public ResponseEntity<List<StockResponse>> getAllStocks() {
        return ResponseEntity.ok(stockService.getAllStocks());
    }

    /**
     * Retrieves a specific stock item by its unique identifier
     *
     * @param id The unique identifier of the stock item
     * @return A response containing the stock item corresponding to the identifier
     */
    @GetMapping("/{id}")
    public ResponseEntity<StockResponse> getStockById(@PathVariable UUID id) {
        return ResponseEntity.ok(stockService.getById(id));
    }

    /**
     * Creates a new stock item in the system
     *
     * @param stockRequest The stock data to be created
     * @return A response containing the newly created stock item
     */
    @PostMapping
    public ResponseEntity<StockResponse> createStock(@RequestBody @Valid StockRequest stockRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(stockService.createEntity(stockRequest));
    }

    /**
     * Updates an existing stock item in the system
     *
     * @param id The unique identifier of the stock item to be updated
     * @param stockRequest The new stock data
     * @return A response containing the updated stock item
     */
    @PutMapping("/{id}")
    public ResponseEntity<StockResponse> updateStock(@PathVariable UUID id,
                                                     @RequestBody StockRequest stockRequest) {
        return ResponseEntity.ok(stockService.updateEntity(stockRequest, id));
    }

    /**
     * Deletes a specific stock item by its unique identifier
     *
     * @param id The unique identifier of the stock item to be deleted
     * @return A response with no content, indicating that the deletion was successful
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStock(@PathVariable UUID id) {
        stockService.deleteEntityById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Retrieves the total stock of a specific product
     *
     * @param productId The product identifier whose stock is to be retrieved
     * @return A map with the total stock for the product
     */
    @GetMapping("/total")
    public ResponseEntity<Map<String, String>> getTotalStock(@RequestParam String productId) {
        return ResponseEntity.ok(stockService.getTotalStock(productId));
    }

    /**
     * Increments the stock quantity of a product
     *
     * @param productId The product identifier
     * @param quantity The quantity to increment in the stock
     * @return A response containing the updated stock item
     */
    @PutMapping("/increment")
    public ResponseEntity<StockResponse> incrementStock(@RequestParam String productId, @RequestParam int quantity) {
        return ResponseEntity.ok(stockService.incrementQuantity(productId, quantity));
    }

    /**
     * Retrieves the oldest stock of a product
     *
     * @param productId The product identifier
     * @return A response containing the oldest stock of the product
     */
    @GetMapping("/oldest")
    public ResponseEntity<StockResponse> getOldestStock(@RequestParam String productId) {
        return ResponseEntity.ok(stockService.getOldestStock(productId));
    }

    /**
     * Retrieves stock that is about to expire within a specific number of days
     *
     * @param days The number of days within which the stock is about to expire
     * @return A list of stock responses that are about to expire
     */
    @GetMapping("/expiring")
    public ResponseEntity<List<StockResponse>> getExpiringStock(@RequestParam Integer days) {
        return ResponseEntity.ok(stockService.getExpiringDate(days));
    }

    /**
     * Checks if there are products whose stock is below a specific threshold
     *
     * @param threshold The minimum stock threshold
     * @param category The product category (optional)
     * @return A list of products whose stock is below the specified threshold
     */
    @GetMapping("/threshold")
    public ResponseEntity<List<Product>> checkStockThreshold(@RequestParam Integer threshold,
                                                             @RequestParam(required = false) String category) {
        return ResponseEntity.ok(stockService.checkStockThreshold(threshold, category));
    }


    /**
     * Retrieves the stock items from a specific supplier
     *
     * @param providerId The unique identifier of the supplier
     * @return A list of stock responses associated with the supplier
     */
    @GetMapping("/provider")
    public ResponseEntity<List<StockResponse>> getStocksByProvider(@RequestParam UUID providerId) {
        return ResponseEntity.ok(stockService.getStocksByProviderId(providerId));
    }

    /**
     * Retrieves the stock items between a specific date range
     *
     * @param startDate The start date of the range
     * @param endDate The end date of the range
     * @return A list of stock responses between the specified dates
     */
    @GetMapping("/between-dates")
    public ResponseEntity<List<StockResponse>> getStocksBetweenDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(stockService.getAllStocksBetweenDates(startDate, endDate));
    }

    /**
     * Retrieves the inventory status based on the provided low stock thresholds
     *
     * @param lowStockThresholds A map with the low stock thresholds for each product
     * @return A map with the inventory status for the products with low stock
     */
    @GetMapping("/status")
    public ResponseEntity<Map<String, StockStatus>> getInventoryStatus(
            @RequestParam Map<String, Integer> lowStockThresholds) {
        Map<String, StockStatus> inventoryStatus = stockService.getInventoryStatus(lowStockThresholds);
        return ResponseEntity.ok(inventoryStatus);
    }
}
