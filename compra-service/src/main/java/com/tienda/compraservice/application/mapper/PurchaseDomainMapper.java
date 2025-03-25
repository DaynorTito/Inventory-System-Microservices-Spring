package com.tienda.compraservice.application.mapper;

import com.tienda.compraservice.domain.model.dto.DetailPurchase;
import com.tienda.compraservice.domain.model.dto.Provider;
import com.tienda.compraservice.domain.model.dto.Purchase;
import com.tienda.compraservice.domain.model.dto.request.CreateCompletePurchase;
import com.tienda.compraservice.domain.model.dto.request.CreatePurchaseRequest;
import com.tienda.compraservice.domain.model.dto.response.DetailPurchaseResponse;
import com.tienda.compraservice.domain.model.dto.response.ProviderResponse;
import com.tienda.compraservice.domain.model.dto.response.PurchasesResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface PurchaseDomainMapper {

    /**
     * Converts a Purchase domain entity to a PurchasesResponse DTO
     *
     * @param purchase the purchase entity to convert
     * @return a PurchasesResponse DTO containing the mapped data
     */
    @Mapping(source = "id", target = "id")
    @Mapping(source = "total", target = "total")
    @Mapping(source = "provider", target = "provider")
    @Mapping(source = "items", target = "items")
    @Mapping(source = "canceled", target = "canceled")
    @Mapping(source = "adquisitionDate", target = "adquisitionDate")
    PurchasesResponse domainToResponse(Purchase purchase);

    /**
     * Converts a DetailPurchase domain entity to a DetailPurchaseResponse DTO
     *
     * @param detailPurchase the detail purchase entity to convert
     * @return a DetailPurchaseResponse DTO containing the mapped data
     */
    @Mapping(source = "id", target = "id")
    @Mapping(source = "productId", target = "productId")
    @Mapping(source = "quantity", target = "quantity")
    @Mapping(source = "unitPrice", target = "unitPrice")
    @Mapping(source = "expirationDate", target = "expirationDate")
    DetailPurchaseResponse detailDomainToResponse(DetailPurchase detailPurchase);

    /**
     * Converts a CreatePurchaseRequest DTO to a Purchase domain entity
     *
     * @param request the DTO containing purchase data
     * @param provider the provider entity associated with the purchase
     * @return a Purchase entity with the mapped data
     */
    default Purchase createPurchaseRequestToDomain(CreatePurchaseRequest request, Provider provider) {
        if (request == null) {
            return null;
        }

        List<DetailPurchase> items = new ArrayList<>();
        for (var item : request.getItems()) {
            DetailPurchase detailPurchase = DetailPurchase.builder()
                    .productId(item.getProductId())
                    .quantity(item.getQuantity())
                    .unitPrice(item.getUnitPrice())
                    .expirationDate(item.getExpirationDate())
                    .build();
            items.add(detailPurchase);
        }

        return Purchase.builder()
                .provider(providerToProviderResponse(provider))
                .items(items.stream().map(this::detailDomainToResponse).toList())
                .canceled(false)
                .build();
    }

    /**
     * Converts a CreateCompletePurchase DTO to a Purchase domain entity
     *
     * @param request the DTO containing complete purchase data
     * @param provider the provider entity associated with the purchase
     * @param productIds the list of product IDs associated with the purchase items
     * @return a Purchase entity with the mapped data
     */
    default Purchase createCompletePurchaseToDomain(CreateCompletePurchase request,
                                                    Provider provider, List<String> productIds) {
        if (request == null)
            return null;

        List<DetailPurchase> items = new ArrayList<>();
        for (int i = 0; i < request.getItems().size(); i++) {
            var item = request.getItems().get(i);
            DetailPurchase detailPurchase = DetailPurchase.builder()
                    .productId(productIds.get(i))
                    .quantity(item.getQuantity())
                    .unitPrice(item.getUnitPrice())
                    .expirationDate(item.getExpirationDate())
                    .build();
            items.add(detailPurchase);
        }

        return Purchase.builder()
                .provider(providerToProviderResponse(provider))
                .items(items.stream().map(this::detailDomainToResponse).toList())
                .canceled(false)
                .build();
    }

    /**
     * Converts a Provider domain entity to a ProviderResponse DTO
     *
     * @param provider the provider entity to convert
     * @return a ProviderResponse DTO containing the mapped data
     */
    default ProviderResponse providerToProviderResponse(Provider provider) {
        return ProviderResponse.builder()
                .id(provider.getId())
                .name(provider.getName())
                .address(provider.getAddress())
                .phone(provider.getPhone())
                .email(provider.getEmail())
                .active(provider.getActive())
                .build();
    }
}
