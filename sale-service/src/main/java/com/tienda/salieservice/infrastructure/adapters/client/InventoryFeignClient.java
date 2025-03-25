package com.tienda.salieservice.infrastructure.adapters.client;

import com.tienda.salieservice.domain.model.dto.request.SaleInventoryRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name = "inventory-service")
public interface InventoryFeignClient {

    @PostMapping("inventory/register-sale")
    void registerOutputInventory(@RequestBody SaleInventoryRequest request);
}
