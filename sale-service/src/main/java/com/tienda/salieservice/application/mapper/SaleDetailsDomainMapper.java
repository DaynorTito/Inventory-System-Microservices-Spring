package com.tienda.salieservice.application.mapper;

import com.tienda.salieservice.application.validator.FeignValidator;
import com.tienda.salieservice.domain.model.dto.Product;
import com.tienda.salieservice.domain.model.dto.SaleDetails;
import com.tienda.salieservice.domain.model.dto.request.SaleDetailsRequest;
import com.tienda.salieservice.domain.model.dto.response.SaleDetailsResponse;
import com.tienda.salieservice.infrastructure.adapters.exception.ValidationException;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public abstract class SaleDetailsDomainMapper {

    @Autowired
    protected FeignValidator feignValidator;

    /**
     * Converts a SaleDetailsRequest to a SaleDetails entity
     *
     * @param request The SaleDetailsRequest object containing sale details
     * @return A SaleDetails entity object populated with the provided data
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productId", source = "productId")
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "unitPrice", expression = "java(getProductPrice(request))")
    @Mapping(target = "subtotal", expression = "java(calculateSubtotal(request))")
    @Mapping(target = "sale", ignore = true)
    public abstract SaleDetails saleDetailsRequestToSaleDetails(SaleDetailsRequest request);

    /**
     * Converts a SaleDetails entity to a SaleDetailsResponse object
     *
     * @param saleDetails The SaleDetails entity
     * @return A SaleDetailsResponse object populated with data from the SaleDetails entity
     */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "productId", source = "productId")
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "unitPrice", source = "unitPrice")
    @Mapping(target = "subtotal", source = "subtotal")
    public abstract SaleDetailsResponse saleDetailsToSaleDetailsResponse(SaleDetails saleDetails);

    /**
     * Retrieves the price of the product, considering any discounts provided in the request
     *
     * @param request The SaleDetailsRequest object containing the product and discount details
     * @return The price of the product, adjusted for any discounts
     */
    @Named("getProductPrice")
    protected BigDecimal getProductPrice(SaleDetailsRequest request) {
        String productId = request.getProductId();
        Product product = feignValidator.verifyExistingProduct(productId);
        BigDecimal discount = request.getDiscount() != null ? request.getDiscount() : BigDecimal.ZERO;
        if (request.getUnitPrice() != null)
            return request.getUnitPrice().subtract(discount);
        return product.getSalePrice().subtract(discount);
    }

    /**
     * Calculates the subtotal for the sale, considering the quantity, unit price, and any discounts
     *
     * @param request The SaleDetailsRequest object containing the product and quantity details
     * @return The calculated subtotal for the sale
     * @throws ValidationException if the discount is greater than the unit price
     */
    @Named("calculateSubtotal")
    protected BigDecimal calculateSubtotal(SaleDetailsRequest request) {
        String productId = request.getProductId();
        Integer quantity = request.getQuantity();
        Product product = feignValidator.verifyExistingProduct(productId);
        BigDecimal unitPrice = request.getUnitPrice() != null ? request.getUnitPrice() : product.getSalePrice();
        BigDecimal discount = request.getDiscount() != null ? request.getDiscount() : BigDecimal.ZERO;
        if (unitPrice.compareTo(discount) < 0)
            throw new ValidationException("El descuento no puede ser mayor que el precio unitariio");
        return unitPrice.subtract(discount).multiply(BigDecimal.valueOf(quantity));
    }
}
