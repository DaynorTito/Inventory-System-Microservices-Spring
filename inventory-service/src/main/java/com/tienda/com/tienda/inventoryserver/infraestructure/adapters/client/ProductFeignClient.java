package com.tienda.com.tienda.inventoryserver.infraestructure.adapters.client;

import com.tienda.com.tienda.inventoryserver.domain.model.dto.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "producto-service")
public interface ProductFeignClient {

    /**
     * Method to get a product by its code from the product service
     *
     * @param cod Product code to fetch the product
     * @return Product object corresponding to the given code
     */
    @GetMapping("/product/{cod}")
    Product getProductByCod(@PathVariable String cod);
}
