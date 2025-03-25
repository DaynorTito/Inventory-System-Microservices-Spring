package com.tienda.compraservice.infraestructure.adapters.entity;


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

@Entity(name = "purchases")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PurchaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private BigDecimal total;

    @ManyToOne
    @JoinColumn(name = "provider_id", nullable = false)
    private ProviderEntity provider;

    @OneToMany(mappedBy = "purchase", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetailPurchaseEntity> items;

    private Boolean canceled;

    @CreationTimestamp
    @Column(name = "adquisition_date", nullable = false)
    private LocalDateTime adquisitionDate;
}
