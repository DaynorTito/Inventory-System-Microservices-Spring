package com.tienda.productoservice.infrastructure.adapters.repository;

import com.tienda.productoservice.infrastructure.adapters.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<ProductEntity, String> {
    List<ProductEntity> findByCategoryNameAndBrandName(String category, String brand);
    List<ProductEntity> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String description);
}
