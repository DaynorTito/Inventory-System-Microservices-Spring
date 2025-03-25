package com.tienda.compraservice.domain.model.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Provider {
    UUID id;
    String name;
    String address;
    String phone;
    String email;
    Boolean active;
}
