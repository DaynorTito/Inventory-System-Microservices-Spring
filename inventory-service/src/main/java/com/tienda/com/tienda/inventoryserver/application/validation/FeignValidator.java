package com.tienda.com.tienda.inventoryserver.application.validation;


import com.tienda.com.tienda.inventoryserver.infraestructure.adapters.client.ProductFeignClient;
import com.tienda.com.tienda.inventoryserver.infraestructure.adapters.client.PurchaseFeignClient;
import com.tienda.com.tienda.inventoryserver.infraestructure.adapters.exception.ValidationException;
import feign.FeignException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.tienda.com.tienda.inventoryserver.infraestructure.adapters.client.ErrorHandler.extractErrorMessage;

@AllArgsConstructor
@Service
public class FeignValidator {

    private final PurchaseFeignClient purchaseFeignCLient;
    private final ProductFeignClient productFeignClient;

    /**
     * Verifies if a product with the given code exists by making a request to the product service
     * If the product does not exist or an error occurs, a validation exception is thrown
     *
     * @param cod The product code.
     * @throws ValidationException if there is an error in retrieving product information
     */
    public void verifyExistingProduct(String cod) {
        try {
            productFeignClient.getProductByCod(cod);
        } catch (FeignException e) {
            throw new ValidationException("Error en informacion del producto: " + extractErrorMessage(e));
        }
    }

    /**
     * Verifies if a provider with the given ID exists by making a request to the provider service
     * If the provider does not exist, or if the ID is invalid, a validation exception is thrown
     *
     * @param cod The provider ID
     * @throws ValidationException if the provider ID is invalid or if there is an error in retrieving provider information
     */
    public void verifyExistingProvider(UUID cod) {
        try {
            UUID providerId = UUID.fromString(cod.toString());
            purchaseFeignCLient.getProviderById(providerId);
        } catch (IllegalArgumentException e) {
            throw new ValidationException("El ID del proveedor no es un UUID valido");
        } catch (FeignException e) {
            throw new ValidationException("Error en informacion del proveedor: " + extractErrorMessage(e));
        }
    }
}
