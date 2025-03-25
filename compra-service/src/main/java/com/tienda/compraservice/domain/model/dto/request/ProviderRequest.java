package com.tienda.compraservice.domain.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProviderRequest {

    @NotBlank(message = "El nombre del proveedor no puede estar vacio.")
    @Size(max = 50, message = "El nombre del proveedor no puede exceder los 50 caracteres.")
    private String name;

    @NotBlank(message = "La direccion no puede estar vacia")
    private String address;

    @NotBlank(message = "El numero de telefono no puede estar vacio")
    @Size(max = 50, message = "El numero de telefono no puede exceder los 50 caracteres")
    private String phone;

    @Email(message = "El correo electrónico debe tener un formato valido")
    @Size(max = 50, message = "El correo electronico no puede exceder los 50 caracteres")
    private String email;

    private Boolean active;
}
