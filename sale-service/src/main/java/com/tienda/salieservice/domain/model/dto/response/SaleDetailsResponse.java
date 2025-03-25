package com.tienda.salieservice.domain.model.dto.response;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SaleDetailsResponse {
    Long id;
    String productId;
    Integer quantity;
    BigDecimal unitPrice;
    BigDecimal subtotal;
}
