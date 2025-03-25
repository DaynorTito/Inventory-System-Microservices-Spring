package com.tienda.com.tienda.inventoryserver.application.mapper;

import com.tienda.com.tienda.inventoryserver.domain.model.dto.Kardex;
import com.tienda.com.tienda.inventoryserver.domain.model.dto.Product;
import com.tienda.com.tienda.inventoryserver.domain.model.dto.request.KardexRequest;
import com.tienda.com.tienda.inventoryserver.domain.model.dto.request.PurchaseInventoryRequest;
import com.tienda.com.tienda.inventoryserver.domain.model.dto.request.SaleInventoryRequest;
import com.tienda.com.tienda.inventoryserver.domain.model.dto.response.KardexResponse;
import com.tienda.com.tienda.inventoryserver.infraestructure.adapters.client.ProductFeignClient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;

import java.util.UUID;

@Mapper(componentModel = "spring")
@Primary
public abstract class KardexDomainMapper {

    @Autowired
    protected ProductFeignClient productFeignClient;

    /**
     * Converts a KardexRequest object to a Kardex entity, ignoring the ID field
     * @param kardexRequest The request object containing Kardex data
     * @return A Kardex entity
     */
    @Mapping(target = "id", ignore = true)
    public abstract Kardex kardexRequestToKardex(KardexRequest kardexRequest);

    /**
     * Converts a Kardex entity to a KardexResponse object, mapping the productId using the kardexToProduct method
     * @param kardex The Kardex entity containing inventory movement data
     * @return A KardexResponse object
     */
    @Mapping(target = "productId", source = "kardex", qualifiedByName = "kardexToProduct")
    public abstract KardexResponse kardexToKardexResponse(Kardex kardex);

    /**
     * Retrieves a Product object by its product ID from the Kardex entity
     * @param kardex The Kardex entity containing the product ID
     * @return A Product object retrieved from the external product service
     */
    @Named("kardexToProduct")
    protected Product kardexToProduct(Kardex kardex) {
        return productFeignClient.getProductByCod(kardex.getProductId());
    }

    /**
     * Converts a string ID to a UUID
     * @param id The string representation of a UUID
     * @return A UUID object or null if the input is null
     */
    @Named("stringToUUID")
    protected UUID stringToUUID(String id) {
        return id != null ? UUID.fromString(id) : null;
    }

    /**
     * Creates a KardexRequest for a purchase movement, mapping fields from a PurchaseInventoryRequest object
     * @param kardexResponse The request object containing purchase inventory data
     * @return A KardexRequest representing the inventory movement
     */
    @Mapping(target = "typeMovement", ignore = true)
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "productId", source = "productId")
    @Mapping(target = "unitPrice", source = "purchaseUnitCost")
    @Mapping(target = "movementDate", ignore = true)
    public abstract KardexRequest createPurchaseKardex(PurchaseInventoryRequest kardexResponse);

    /**
     * Creates a KardexRequest for a sale movement, mapping fields from a SaleInventoryRequest object
     * @param kardexResponse The request object containing sale inventory data
     * @return A KardexRequest representing the inventory movement
     */
    @Mapping(target = "typeMovement", ignore = true)
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "productId", source = "productId")
    @Mapping(target = "unitPrice", source = "unitPrice")
    @Mapping(target = "movementDate", ignore = true)
    public abstract KardexRequest createSaleKardex(SaleInventoryRequest kardexResponse);
}
