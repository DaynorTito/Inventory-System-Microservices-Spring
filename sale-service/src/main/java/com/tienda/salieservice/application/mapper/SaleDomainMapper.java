package com.tienda.salieservice.application.mapper;

import com.tienda.salieservice.application.validator.FeignValidator;
import com.tienda.salieservice.domain.model.dto.Sale;
import com.tienda.salieservice.domain.model.dto.request.SaleDetailsRequest;
import com.tienda.salieservice.domain.model.dto.request.SaleRequest;
import com.tienda.salieservice.domain.model.dto.response.SaleResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring", uses = {SaleDetailsDomainMapper.class})
public abstract class SaleDomainMapper {

    @Autowired
    protected FeignValidator feignValidator;

    /**
     * Converts a SaleRequest to a Sale entity
     *
     * @param request The SaleRequest object containing sale information
     * @return A Sale entity populated with the provided data
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "saleDate", expression = "java(getCurrentDateTime())")
    @Mapping(target = "totalAmount", expression = "java(calculateTotalAmount(request.getSaleDetails()))")
    @Mapping(target = "saleDetails", source = "saleDetails")
    @Mapping(target = "totalDiscount", expression = "java(calculateTotalDiscount(request.getSaleDetails()))")
    public abstract Sale saleRequestToSale(SaleRequest request);

    /**
     * Converts a Sale entity to a SaleResponse object
     *
     * @param sale The Sale entity
     * @return A SaleResponse object populated with data from the Sale entity
     */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "saleDate", source = "saleDate")
    @Mapping(target = "totalAmount", source = "totalAmount")
    @Mapping(target = "customerName", source = "customerName")
    @Mapping(target = "paymentMethod", source = "paymentMethod")
    @Mapping(target = "totalDiscount", source = "totalDiscount")
    @Mapping(target = "saleDetails", source = "saleDetails")
    public abstract SaleResponse saleToSaleResponse(Sale sale);

    /**
     * Retrieves the current date and time
     *
     * @return The current date and time
     */
    protected LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }

    /**
     * Calculates the total amount for the sale based on sale details
     *
     * @param details The list of SaleDetailsRequest objects
     * @return The total amount of the sale, considering all details
     */
    @Named("calculateTotalAmount")
    protected BigDecimal calculateTotalAmount(List<SaleDetailsRequest> details) {
        if (details == null || details.isEmpty()) {
            return BigDecimal.ZERO;
        }
        BigDecimal total = BigDecimal.ZERO;
        for (var detail : details) {
            var product = feignValidator.verifyExistingProduct(detail.getProductId());
            BigDecimal unitPrice = detail.getUnitPrice() != null ? detail.getUnitPrice() : product.getSalePrice();
            BigDecimal discount = detail.getDiscount() != null ? detail.getDiscount() : BigDecimal.ZERO;
            BigDecimal subtotal = unitPrice.subtract(discount).multiply(BigDecimal.valueOf(detail.getQuantity()));

            total = total.add(subtotal);
        }

        return total;
    }

    /**
     * Calculates the total discount for the sale based on sale details
     *
     * @param details The list of SaleDetailsRequest objects
     * @return The total discount of the sale, considering all details
     */
    @Named("calculateTotalDiscount")
    protected BigDecimal calculateTotalDiscount(List<SaleDetailsRequest> details) {
        if (details == null || details.isEmpty())
            return BigDecimal.ZERO;
        var total = BigDecimal.ZERO;
        for (var detail : details) {
            var product = feignValidator.verifyExistingProduct(detail.getProductId());
            var discount = detail.getDiscount() != null ? detail.getDiscount() : product.getDiscount();
            if (discount == null) discount = BigDecimal.ZERO;
            var quantity = detail.getQuantity();
            total = total.add(discount.multiply(BigDecimal.valueOf(quantity)));
        }
        return total;
    }
}
