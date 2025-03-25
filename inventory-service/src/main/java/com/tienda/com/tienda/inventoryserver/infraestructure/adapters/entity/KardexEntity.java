package com.tienda.com.tienda.inventoryserver.infraestructure.adapters.entity;


import com.tienda.com.tienda.inventoryserver.domain.model.constant.TypeMove;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity(name = "kardex")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class KardexEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, name = "type_movement")
    @Enumerated(EnumType.STRING)
    private TypeMove typeMovement;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, name = "product_id")
    private String productId;

    @Column(nullable = false, name = "unit_price")
    private BigDecimal unitPrice;

    @Column(nullable = false, name = "total_price")
    private BigDecimal totalPrice;

    @Column(nullable = false, name = "movement_date")
    private LocalDate movementDate;
}
