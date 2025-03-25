package com.tienda.compraservice.application.validator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tienda.compraservice.domain.model.dto.request.ProductRequest;
import com.tienda.compraservice.domain.model.dto.request.PurchaseInventoryRequest;
import com.tienda.compraservice.domain.model.dto.response.ProductResponse;
import com.tienda.compraservice.infraestructure.adapters.client.InventoryFeignClient;
import com.tienda.compraservice.infraestructure.adapters.client.ProductFeignClient;
import com.tienda.compraservice.infraestructure.adapters.exception.ValidationException;
import feign.FeignException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class FeignValidator {

    private final InventoryFeignClient inventoryFeignClient;
    private final ProductFeignClient productFeignClient;

    /**
     * Registers a purchase in the inventory system
     *
     * @param request The purchase inventory request
     */
    public void registerPurchase(PurchaseInventoryRequest request) {
        try {
            inventoryFeignClient.registerInputInventory(request);
        } catch (FeignException e) {
            throw new ValidationException("Error en informacion del producto: " + extractErrorMessage(e));
        }
    }

    /**
     * Extracts the error message from a Feign exception
     *
     * @param e The Feign exception
     * @return The error message
     */
    public static String extractErrorMessage(FeignException e) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(e.contentUTF8());
            return jsonNode.has("userMessage") ? jsonNode.get("userMessage").asText() : "Error desconocido";
        } catch (Exception ex) {
            return "Error desconocido";
        }
    }

    /**
     * Creates a new product in the external service
     *
     * @param productReques The product request
     * @return The created product response
     */
    public ProductResponse createProductIntoService(ProductRequest productReques){
        try {
            return productFeignClient.createProduct(productReques);
        } catch (FeignException e) {
            throw new ValidationException("Error al crear el producto: " + extractErrorMessage(e));
        }
    }

    /**
     * Verifies if a product exists in the external service by its code
     *
     * @param cod The product code
     */
    public void verifyProductIntoService(String cod){
        try {
            productFeignClient.getProductByCod(cod);
        } catch (FeignException e) {
            throw new ValidationException("Error al crear el producto: " + extractErrorMessage(e));
        }
    }
}
