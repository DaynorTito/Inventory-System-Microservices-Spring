package com.tienda.compraservice.domain.model.dto.response;

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
public class PurchasesResponse {
    UUID id;
    BigDecimal total;
    ProviderResponse provider;
    List<DetailPurchaseResponse> items;
    Boolean canceled;
    LocalDateTime adquisitionDate;
}