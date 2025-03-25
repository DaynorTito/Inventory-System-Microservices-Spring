package com.tienda.com.tienda.inventoryserver.infraestructure.adapters.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "compra-service")
public interface PurchaseFeignClient {

    /**
     * Method to get the provider by its ID from the purchase service
     *
     * @param id Provider ID to fetch the provider
     * @return Provider information corresponding to the given ID
     */
    @GetMapping("/provider/{id}")
    String getProviderById(@PathVariable UUID id);
}
