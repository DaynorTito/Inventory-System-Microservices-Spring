package com.tienda.com.tienda.inventoryserver.infraestructure.rest.controller;


import com.tienda.com.tienda.inventoryserver.application.services.ManagementInventory;
import com.tienda.com.tienda.inventoryserver.domain.model.dto.request.PurchaseInventoryRequest;
import com.tienda.com.tienda.inventoryserver.domain.model.dto.request.SaleInventoryRequest;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inventory")
@AllArgsConstructor
public class MovementsController {
    private final ManagementInventory managementInventory;

    /**
     * Registers an inventory input for a purchase
     * This endpoint registers an inventory movement corresponding to a product purchase
     *
     * @param request The purchase data that will be recorded in the inventory
     * @return A message confirming the operation performed
     */
    @PostMapping("/register-purchase")
    public String registerInputInventory(@RequestBody PurchaseInventoryRequest request) {
        return managementInventory.registerInputInventory(request);
    }

    /**
     * Registers an inventory output for a sale
     * This endpoint registers an inventory movement corresponding to a product sale
     *
     * @param request The sale data that will be recorded in the inventory
     * @return A message confirming the operation performed
     */
    @PostMapping("/register-sale")
    public String registerOutputInventory(@RequestBody SaleInventoryRequest request) {
        return managementInventory.registerOutputInventory(request);
    }
}
