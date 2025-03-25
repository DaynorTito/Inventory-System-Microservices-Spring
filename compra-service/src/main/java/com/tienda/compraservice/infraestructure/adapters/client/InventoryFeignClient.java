package com.tienda.compraservice.infraestructure.adapters.client;


import com.tienda.compraservice.domain.model.dto.request.PurchaseInventoryRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "inventory-service")
public interface InventoryFeignClient {

    /**
     * Registers a purchase in the inventory service
     *
     * @param request The request object containing purchase details
     * @return A string response indicating the result of the registration
     */
    @PostMapping("inventory/register-purchase")
    String registerInputInventory(@RequestBody PurchaseInventoryRequest request);
}
