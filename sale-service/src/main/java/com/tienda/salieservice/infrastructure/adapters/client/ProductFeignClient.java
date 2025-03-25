package com.tienda.salieservice.infrastructure.adapters.client;

import com.tienda.salieservice.domain.model.dto.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "producto-service")
public interface ProductFeignClient {

    @GetMapping("/product/{cod}")
    Product getProductByCod(@PathVariable String cod);
}
