package com.tienda.com.tienda.inventoryserver.infraestructure.adapters.adapterimpl;

import com.tienda.com.tienda.inventoryserver.domain.model.constant.TypeMove;
import com.tienda.com.tienda.inventoryserver.domain.model.dto.Kardex;
import com.tienda.com.tienda.inventoryserver.domain.model.dto.TopSellingProduct;
import com.tienda.com.tienda.inventoryserver.domain.port.KardexPersistancePort;
import com.tienda.com.tienda.inventoryserver.infraestructure.adapters.exception.KardexNotFoundException;
import com.tienda.com.tienda.inventoryserver.infraestructure.adapters.mapper.KardexMapper;
import com.tienda.com.tienda.inventoryserver.infraestructure.adapters.repository.KardexRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class KardexPersistanceAdapter implements KardexPersistancePort {

    private final KardexRepository kardexRepository;
    private final KardexMapper kardexMapper;

    /**
     * Retrieves all Kardex records for a given product ID
     *
     * @param productId The product ID to search for
     * @return A list of Kardex records related to the provided product ID
     */
    @Override
    public List<Kardex> findAllKardexByProductId(String productId) {
        var kardex = kardexRepository.findAllByProductIdOrderByMovementDateDesc(productId);
        return kardex.stream().map(kardexMapper::toDomain).toList();
    }

    /**
     * Retrieves all Kardex records within a specified date range
     *
     * @param after The start date of the range
     * @param before The end date of the range
     * @return A list of Kardex records that fall within the provided date range
     */
    @Override
    public List<Kardex> findAllKardexByMovementDateBetween(LocalDate after, LocalDate before) {
        var kardex = kardexRepository.findAllByMovementDateBetween(after, before);
        return kardex.stream().map(kardexMapper::toDomain).toList();
    }

    /**
     * Retrieves all Kardex records of a specified movement type
     *
     * @param typeMovement The type of movement to filter by
     * @return A list of Kardex records that match the specified movement type
     */
    @Override
    public List<Kardex> findAllKardexByTypeMovement(TypeMove typeMovement) {
        var kardex = kardexRepository.findAllByTypeMovement(typeMovement);
        return kardex.stream().map(kardexMapper::toDomain).toList();
    }

    /**
     * Retrieves all Kardex records within a specified date range for a specific product ID
     *
     * @param after The start date of the range
     * @param before The end date of the range
     * @param productId The product ID to filter by
     * @return A list of Kardex records within the date range for the specified product
     */
    @Override
    public List<Kardex> findAllKardexByMovementDateBetweenAndProductId(LocalDate after, LocalDate before, String productId) {
        var kardex = kardexRepository.findAllByMovementDateBetweenAndProductId(after, before, productId);
        return kardex.stream().map(kardexMapper::toDomain).toList();
    }

    /**
     * Retrieves the top-selling products within a specified date range
     *
     * @param after The start date of the range
     * @param before The end date of the range
     * @param limit The maximum number of top-selling products to return
     * @return A list of the top-selling products within the specified date range
     */
    @Override
    public List<TopSellingProduct> findTopSellingProducts(LocalDate after, LocalDate before, int limit) {
        return kardexRepository.findTopSellingProducts(after, before, PageRequest.of(0, limit))
                .stream()
                .map(objects -> new TopSellingProduct(
                        (String) objects[0],    // productId
                        ((Number) objects[1]).intValue() // totalSold
                ))
                .toList();
    }

    /**
     * Calculates the total earnings within a specified date range
     *
     * @param startDate The start date of the range
     * @param endDate The end date of the range
     * @return The total earnings within the specified date range
     */
    @Override
    public BigDecimal calculateEarningsBetweenDates(LocalDate startDate, LocalDate endDate) {
        return kardexRepository.calculateEarningsBetweenDates(startDate, endDate);
    }

    /**
     * Retrieves the last Kardex record for a given product ID
     *
     * @param productId The product ID to search for
     * @return The last Kardex record for the provided product ID
     */
    @Override
    public Kardex getLastKardexByProductId(String productId) {
        var kardex = kardexRepository.findFirstByProductIdOrderByMovementDateDesc(productId);
        return kardexMapper.toDomain(kardex);
    }

    /**
     * Retrieves a Kardex record by its UUID
     *
     * @param uuid The UUID of the Kardex record
     * @return The Kardex record with the provided UUID
     * @throws KardexNotFoundException if no Kardex record is found with the given UUID
     */
    @Override
    public Kardex readById(UUID uuid) {
        var kardex = kardexRepository.findById(uuid).orElseThrow(
                () -> new KardexNotFoundException("Registro no encontrado"));
        return kardexMapper.toDomain(kardex);
    }

    /**
     * Creates a new Kardex record
     *
     * @param request The Kardex record to create
     * @return The created Kardex record
     */
    @Override
    public Kardex create(Kardex request) {
        var kardexEntity = kardexMapper.toEntity(request);
        return kardexMapper.toDomain(kardexRepository.save(kardexEntity));
    }

    /**
     * Updates an existing Kardex record
     *
     * @param request The updated Kardex record
     * @param uuid The UUID of the Kardex record to update
     * @return The updated Kardex record
     * @throws KardexNotFoundException if no Kardex record is found with the given UUID
     */
    @Override
    public Kardex update(Kardex request, UUID uuid) {
        var kardexToUpdate = kardexRepository.findById(uuid).orElseThrow(
                () -> new KardexNotFoundException("Registro no encontrado")
        );
        var kardexEntity = kardexMapper.toEntity(request);
        UpdateHelper.updateNonNullFields(kardexEntity, kardexToUpdate);
        return kardexMapper.toDomain(kardexRepository.save(kardexToUpdate));
    }

    /**
     * Deletes a Kardex record by its UUID
     *
     * @param uuid The UUID of the Kardex record to delete
     */
    @Override
    public void deleteById(UUID uuid) {
        kardexRepository.deleteById(uuid);
    }
}
