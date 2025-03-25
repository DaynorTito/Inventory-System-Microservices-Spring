package com.tienda.com.tienda.inventoryserver.infraestructure.adapters.repository;

import com.tienda.com.tienda.inventoryserver.domain.model.constant.TypeMove;
import com.tienda.com.tienda.inventoryserver.infraestructure.adapters.entity.KardexEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface KardexRepository extends JpaRepository<KardexEntity, UUID> {
    List<KardexEntity> findAllByProductIdOrderByMovementDateDesc(String productId);
    List<KardexEntity> findAllByMovementDateBetween(LocalDate movementDateAfter, LocalDate movementDateBefore);
    List<KardexEntity> findAllByTypeMovement(TypeMove typeMovement);
    List<KardexEntity> findAllByMovementDateBetweenAndProductId(LocalDate after, LocalDate before, String productId);

    @Query("""
    SELECT k.productId, SUM(k.quantity) AS totalSold 
    FROM kardex k 
    WHERE k.movementDate BETWEEN :after AND :before 
    AND k.typeMovement = 'OUTCOME' 
    GROUP BY k.productId 
    ORDER BY totalSold DESC
""")
    List<Object[]> findTopSellingProducts(@Param("after") LocalDate after, @Param("before") LocalDate before, Pageable pageable);

    KardexEntity findFirstByProductIdOrderByMovementDateDesc(String productId);
    @Query("""
        SELECT SUM(k.totalPrice) 
        FROM kardex k 
        WHERE k.movementDate BETWEEN :startDate AND :endDate 
        AND k.typeMovement = 'OUTCOME'
    """)
    BigDecimal calculateEarningsBetweenDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
