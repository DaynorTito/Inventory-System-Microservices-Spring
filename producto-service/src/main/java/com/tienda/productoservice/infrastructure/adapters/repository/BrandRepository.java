package com.tienda.productoservice.infrastructure.adapters.repository;


import com.tienda.productoservice.infrastructure.adapters.entity.BrandEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BrandRepository extends JpaRepository<BrandEntity, Long> {
    Optional<BrandEntity> findByName(String name);
}
