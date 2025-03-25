package com.tienda.compraservice.infraestructure.adapters.repository;

import com.tienda.compraservice.infraestructure.adapters.entity.PurchaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PurchaseRepository extends JpaRepository<PurchaseEntity, UUID> {
}
