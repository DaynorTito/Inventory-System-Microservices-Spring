package com.tienda.compraservice.domain.model.dto;

import com.tienda.compraservice.domain.model.dto.response.DetailPurchaseResponse;
import com.tienda.compraservice.domain.model.dto.response.ProviderResponse;
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
public class Purchase {
    UUID id;
    BigDecimal total;
    ProviderResponse provider;
    List<DetailPurchaseResponse> items;
    Boolean canceled;
    LocalDateTime adquisitionDate;
}
