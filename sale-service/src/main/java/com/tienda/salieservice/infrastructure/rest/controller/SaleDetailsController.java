package com.tienda.salieservice.infrastructure.rest.controller;


import com.tienda.salieservice.application.services.SaleDetailsService;
import com.tienda.salieservice.domain.model.dto.request.SaleDetailsRequest;
import com.tienda.salieservice.domain.model.dto.response.SaleDetailsResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sale/sale-detail")
@AllArgsConstructor
public class SaleDetailsController {

    private final SaleDetailsService saleDetailsService;

    /**
     * Retrieves the sale detail by its unique identifier
     *
     * @param id The unique identifier of the sale detail
     * @return A ResponseEntity containing the SaleDetailsResponse with the requested sale detail
     */
    @GetMapping("/{id}")
    public ResponseEntity<SaleDetailsResponse> getSaleDetailById(@PathVariable Long id) {
        var saleDetailResponse = saleDetailsService.getById(id);
        return ResponseEntity.ok(saleDetailResponse);
    }

    /**
     * Creates a new sale detail
     *
     * @param saleDetailRequest The details of the sale to create
     * @return A ResponseEntity containing the SaleDetailsResponse with the created sale detail
     */
    @PostMapping
    public ResponseEntity<SaleDetailsResponse> createSaleDetail(@RequestBody @Valid
                                                                    SaleDetailsRequest saleDetailRequest) {
        var saleDetailResponse = saleDetailsService.createEntity(saleDetailRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(saleDetailResponse);
    }

    /**
     * Updates an existing sale detail by its ID
     *
     * @param id The unique identifier of the sale detail to update
     * @param saleDetailRequest The updated sale details
     * @return A ResponseEntity containing the SaleDetailsResponse with the updated sale detail
     */
    @PutMapping("/{id}")
    public ResponseEntity<SaleDetailsResponse> updateSaleDetail(@PathVariable Long id,
                                                                @RequestBody @Valid SaleDetailsRequest saleDetailRequest) {
        var saleDetailResponse = saleDetailsService.updateEntity(saleDetailRequest, id);
        return ResponseEntity.ok(saleDetailResponse);
    }

    /**
     * Deletes a sale detail by its ID
     *
     * @param id The unique identifier of the sale detail to delete
     * @return A ResponseEntity with no content to confirm the deletion
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSaleDetail(@PathVariable Long id) {
        saleDetailsService.deleteEntityById(id);
        return ResponseEntity.noContent().build();
    }
}
