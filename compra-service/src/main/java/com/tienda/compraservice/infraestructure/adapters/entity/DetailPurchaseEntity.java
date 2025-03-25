package com.tienda.compraservice.infraestructure.adapters.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity(name = "detail_purchases")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class DetailPurchaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String productId;

    @ManyToOne
    @JoinColumn(name = "purchase_id", nullable = false)
    private PurchaseEntity purchase;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    @Column(nullable = false)
    private BigDecimal unitPrice;
}
