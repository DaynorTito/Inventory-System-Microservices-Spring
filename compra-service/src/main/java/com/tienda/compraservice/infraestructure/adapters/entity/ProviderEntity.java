package com.tienda.compraservice.infraestructure.adapters.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity(name = "providers")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ProviderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 50, unique = true)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false, length = 50)
    private String phone;

    @Column(length = 50)
    private String email;

    private Boolean active;
}
