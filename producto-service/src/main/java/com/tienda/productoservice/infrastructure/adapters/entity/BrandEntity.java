package com.tienda.productoservice.infrastructure.adapters.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity(name = "brands")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BrandEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50, unique = true)
    private String name;

    private String description;

    @OneToMany(mappedBy = "brand", fetch = FetchType.LAZY)
    private List<ProductEntity> products;

}
