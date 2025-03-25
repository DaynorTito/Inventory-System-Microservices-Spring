package com.tienda.compraservice.infraestructure.adapters.repository;

import com.tienda.compraservice.infraestructure.adapters.entity.ProviderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProviderRepository extends JpaRepository<ProviderEntity, UUID> {
    Optional<ProviderEntity> findByName(String name);
}
