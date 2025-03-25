package com.tienda.salieservice.domain.model.dto.response;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SaleResponse {
    UUID id;
    LocalDateTime saleDate;
    BigDecimal totalAmount;
    String customerName;
    String paymentMethod;
    BigDecimal totalDiscount;
    List<SaleDetailsResponse> saleDetails;
}
