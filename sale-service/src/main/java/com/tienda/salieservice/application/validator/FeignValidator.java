package com.tienda.salieservice.application.validator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tienda.salieservice.domain.model.dto.Product;
import com.tienda.salieservice.domain.model.dto.request.SaleInventoryRequest;
import com.tienda.salieservice.infrastructure.adapters.client.InventoryFeignClient;
import com.tienda.salieservice.infrastructure.adapters.client.ProductFeignClient;
import com.tienda.salieservice.infrastructure.adapters.exception.ValidationException;
import feign.FeignException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class FeignValidator {

    private final ProductFeignClient productFeignClient;
    private final InventoryFeignClient inventoryFeignClient;

    /**
     * Verifies if the product exists by its code
     *
     * @param cod the product code
     * @return the product if it exists
     */
    public Product verifyExistingProduct(String cod) {
        try {
            return productFeignClient.getProductByCod(cod);
        } catch (FeignException e) {
            throw new ValidationException("Error con el producto: " + cod + " " + extractErrorMessage(e));
        }
    }

    /**
     * Extracts the error message from a FeignException
     *
     * @param e the FeignException
     * @return the error message extracted from the exception
     */
    public static String extractErrorMessage(FeignException e) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(e.contentUTF8());
            String errorMessage = jsonNode.has("error") ? jsonNode.get("error").asText() : "Error desconocido";
            String userMessage = jsonNode.has("userMessage") ? jsonNode.get("userMessage").asText() : "Error";
            return errorMessage + " " + userMessage;
        } catch (Exception ex) {
            return "Error desconocido";
        }
    }

    /**
     * Registers the sale information in the inventory system.\
     *
     * @param request the sale inventory request
     */
    public void registerSale(SaleInventoryRequest request) {
        try {
            inventoryFeignClient.registerOutputInventory(request);
        } catch (FeignException e) {
            throw new ValidationException("Error en informacion del producto: "+ request.getProductId() +
                    " " + extractErrorMessage(e));
        }
    }
}
