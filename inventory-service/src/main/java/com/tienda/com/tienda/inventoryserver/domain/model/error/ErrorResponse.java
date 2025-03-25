package com.tienda.com.tienda.inventoryserver.domain.model.error;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ErrorResponse {
    String error;
    String userMessage;
    String status;
    Integer code;
}
