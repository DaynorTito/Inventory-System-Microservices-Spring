package com.tienda.salieservice.infrastructure.adapters.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity(name = "sales")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SaleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, name = "sale_date")
    @CreationTimestamp
    private LocalDateTime saleDate;

    @Column(nullable = false, name = "total_amount")
    private BigDecimal totalAmount;

    @Column(length = 50, name = "customer_name", nullable = false)
    private String customerName;

    @Column(length = 50, name = "payment_method", nullable = false)
    private String paymentMethod;

    @Column(name = "discount_percentage")
    private BigDecimal totalDiscount;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SaleDetailsEntity> saleDetails;
}
