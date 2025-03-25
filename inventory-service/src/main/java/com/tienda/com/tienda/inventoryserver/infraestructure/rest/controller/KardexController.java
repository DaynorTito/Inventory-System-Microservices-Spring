package com.tienda.com.tienda.inventoryserver.infraestructure.rest.controller;


import com.tienda.com.tienda.inventoryserver.application.services.EarningsReport;
import com.tienda.com.tienda.inventoryserver.application.services.KardexService;
import com.tienda.com.tienda.inventoryserver.domain.model.dto.request.KardexRequest;
import com.tienda.com.tienda.inventoryserver.domain.model.dto.response.KardexResponse;
import com.tienda.com.tienda.inventoryserver.domain.model.dto.response.ReportInventoryResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/kardex")
@AllArgsConstructor
public class KardexController {

    private final KardexService kardexService;
    private final EarningsReport earningsReport;

    /**
     * Endpoint to retrieve the history of product movements by product ID
     *
     * @param productId The ID of the product whose history is to be fetched
     * @return A ResponseEntity containing the list of KardexResponse objects representing the product's movement history
     */
    @GetMapping("/product-history/{productId}")
    public ResponseEntity<List<KardexResponse>> getProductHistory(@PathVariable String productId) {
        return ResponseEntity.ok(kardexService.getProductHistory(productId));
    }

    /**
     * Endpoint to retrieve inventory movements between the specified date range
     *
     * @param startDate The start date for the date range
     * @param endDate The end date for the date range
     * @return A ResponseEntity containing the list of KardexResponse objects representing the
     *          inventory movements within the date range
     */
    @GetMapping("/inventory-movements")
    public ResponseEntity<List<KardexResponse>> getInventoryMovements(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        return ResponseEntity.ok(kardexService.getInventoryMovements(startDate, endDate));
    }

    /**
     * Endpoint to retrieve inventory movements for a specific product within a date range
     *
     * @param startDate The start date for the date range
     * @param endDate The end date for the date range
     * @param productId The ID of the product whose inventory movements are to be fetched
     * @return A ResponseEntity containing the list of KardexResponse objects representing the
     *          inventory movements for the product within the date range
     */
    @GetMapping("/inventory-movements/{productId}")
    public ResponseEntity<List<KardexResponse>> getInventoryMovements(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @PathVariable String productId) {
        return ResponseEntity.ok(kardexService.getInventoryMovements(startDate, endDate, productId));
    }

    /**
     * Endpoint to retrieve a report of the most sold products within a date range
     *
     * @param limit The maximum number of products to include in the report
     * @param startDate The start date for the date range
     * @param endDate The end date for the date range
     * @return A ResponseEntity containing the report of the most sold products within the date range
     */
    @GetMapping("/most-sold-products")
    public ResponseEntity<List<?>> getMostSoldProductsReport(
            @RequestParam Integer limit,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(kardexService.getMostSoldProductsReport(startDate, endDate, limit));
    }

    /**
     * Endpoint to retrieve a report of the most sold products in the current month
     *
     * @param limit The maximum number of products to include in the report
     * @return A ResponseEntity containing the report of the most sold products in the current month
     */
    @GetMapping("/most-sold-products-month")
    public ResponseEntity<List<?>> getMostSoldProductsReportMonth(@RequestParam Integer limit) {
        return ResponseEntity.ok(kardexService.getMostSoldProductsReport(limit));
    }

    /**
     * Endpoint to retrieve a Kardex entity by its UUID
     *
     * @param uuid The UUID of the Kardex entity to be fetched
     * @return A ResponseEntity containing the KardexResponse object representing the Kardex entity
     */
    @GetMapping("/{uuid}")
    public ResponseEntity<KardexResponse> getById(@PathVariable UUID uuid) {
        return ResponseEntity.ok(kardexService.getById(uuid));
    }

    /**
     * Endpoint to create a new Kardex entity
     *
     * @param request The KardexRequest object containing the data for the new Kardex entity
     * @return A ResponseEntity containing the KardexResponse object representing the created Kardex entity
     */
    @PostMapping
    public ResponseEntity<KardexResponse> createEntity(@RequestBody @Valid KardexRequest request) {
        return ResponseEntity.ok(kardexService.createEntity(request));
    }

    /**
     * Endpoint to update an existing Kardex entity by its UUID
     *
     * @param request The KardexRequest object containing the updated data for the Kardex entity
     * @param uuid The UUID of the Kardex entity to be updated
     * @return A ResponseEntity containing the KardexResponse object representing the updated Kardex entity
     */
    @PutMapping("/{uuid}")
    public ResponseEntity<KardexResponse> updateEntity(@RequestBody KardexRequest request,
                                                       @PathVariable UUID uuid) {
        return ResponseEntity.ok(kardexService.updateEntity(request, uuid));
    }

    /**
     * Endpoint to delete a Kardex entity by its UUID
     *
     * @param uuid The UUID of the Kardex entity to be deleted
     * @return A ResponseEntity with no content indicating that the Kardex entity was successfully deleted
     */
    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deleteEntityById(@PathVariable UUID uuid) {
        kardexService.deleteEntityById(uuid);
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint to retrieve an earnings report for products between two dates
     *
     * @param startDate The start date for the earnings report
     * @param endDate The end date for the earnings report
     * @return A ResponseEntity containing the earnings report response
     */
    @GetMapping("/earnings-report")
    public ResponseEntity<ReportInventoryResponse> earningsBetweenDatesDetailsProducts(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(earningsReport.earningsBetweenDatesDetailsProducts(startDate, endDate));
    }
}
