package com.tienda.salieservice.infrastructure.adapters.repository;

import com.tienda.salieservice.infrastructure.adapters.entity.SaleDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleDetailsRepository extends JpaRepository<SaleDetailsEntity, Long> {
}
