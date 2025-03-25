package com.tienda.productoservice.infrastructure.adapters.repository;


import com.tienda.productoservice.infrastructure.adapters.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    Optional<CategoryEntity> findByName(String name);
}
