package com.tienda.compraservice.infraestructure.adapters.repository;

import com.tienda.compraservice.infraestructure.adapters.entity.DetailPurchaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DetailPurchaseRepository extends JpaRepository<DetailPurchaseEntity, UUID> {
}
