package com.tienda.productoservice.infrastructure.adapters.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity(name = "products")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ProductEntity {

    @Id
    @Column(length = 36)
    private String cod;

    @Column(nullable = false, length = 50)
    private String name;

    private String description;

    @Column(columnDefinition = "DECIMAL(10,2) DEFAULT 0.00")
    private BigDecimal discount;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private BrandEntity brand;

    @Column(name = "sale_price", nullable = false)
    private BigDecimal salePrice;

    @CreationTimestamp
    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate;
}
