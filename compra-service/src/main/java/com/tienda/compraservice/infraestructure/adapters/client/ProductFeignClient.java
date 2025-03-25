package com.tienda.compraservice.infraestructure.adapters.client;


import com.tienda.compraservice.domain.model.dto.request.ProductRequest;
import com.tienda.compraservice.domain.model.dto.response.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "producto-service")
public interface ProductFeignClient {

    /**
     * Creates a new product on Product service
     *
     * @param productRequest The request object containing product details
     * @return The response object with product details
     */
    @PostMapping("/product")
    ProductResponse createProduct(@RequestBody ProductRequest productRequest);

    /**
     * Gets product details by product code of Product service
     *
     * @param cod The product code
     * @return The response object with product details
     */
    @GetMapping("/product/{cod}")
    ProductResponse getProductByCod(@PathVariable String cod);
}
