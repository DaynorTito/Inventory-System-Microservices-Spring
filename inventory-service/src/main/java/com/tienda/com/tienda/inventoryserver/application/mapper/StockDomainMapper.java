package com.tienda.com.tienda.inventoryserver.application.mapper;


import com.tienda.com.tienda.inventoryserver.domain.model.dto.Product;
import com.tienda.com.tienda.inventoryserver.domain.model.dto.Stock;
import com.tienda.com.tienda.inventoryserver.domain.model.dto.request.PurchaseInventoryRequest;
import com.tienda.com.tienda.inventoryserver.domain.model.dto.request.StockRequest;
import com.tienda.com.tienda.inventoryserver.domain.model.dto.response.StockResponse;
import com.tienda.com.tienda.inventoryserver.infraestructure.adapters.client.ProductFeignClient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;

@Mapper(componentModel = "spring")
@Primary
public abstract class StockDomainMapper {

    @Autowired
    protected ProductFeignClient productFeignClient;

    /**
     * Converts a StockRequest object to a Stock entity, ignoring the ID field
     * @param stockRequest The request object containing stock data
     * @return A Stock entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productId", source = "productId")
    public abstract Stock stockRequestToStock(StockRequest stockRequest);

    /**
     * Converts a Stock entity to a StockResponse object, mapping the productId using the stockToProduct
     * @param stock The Stock entity containing inventory details
     * @return A StockResponse object
     */
    @Mapping(target = "productId", source = "stock", qualifiedByName = "stockToProduct")
    public abstract StockResponse stockToStockResponse(Stock stock);

    /**
     * Retrieves a Product object by its product ID from the Stock entity
     * @param stock The Stock entity containing the product ID
     * @return A Product object retrieved from the external product service
     */
    @Named("stockToProduct")
    protected Product stockToProduct(Stock stock) {
        return productFeignClient.getProductByCod(stock.getProductId());
    }

    /**
     * Creates a StockRequest for registering a purchase, mapping fields from a PurchaseInventoryRequest
     * @param request The request object containing purchase inventory data
     * @return A StockRequest representing the inventory purchase
     */
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "purchaseUnitCost", source = "purchaseUnitCost")
    @Mapping(target = "providerId", source = "providerId")
    @Mapping(target = "productId", source = "productId")
    @Mapping(target = "expiryDate", source = "expiryDate")
    @Mapping(target = "purchaseDate", ignore = true)
    public abstract StockRequest registerPurchase(PurchaseInventoryRequest request);
}
