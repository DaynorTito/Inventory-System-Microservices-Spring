package com.tienda.salieservice.infrastructure.adapters.adaptersimpl;

import com.tienda.salieservice.domain.model.dto.SaleDetails;
import com.tienda.salieservice.domain.port.SaleDetailsPersistancePort;
import com.tienda.salieservice.infrastructure.adapters.exception.SaleDetailNotFound;
import com.tienda.salieservice.infrastructure.adapters.mapper.SaleDetailsMapper;
import com.tienda.salieservice.infrastructure.adapters.repository.SaleDetailsRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Transactional
@Service
@AllArgsConstructor
public class SaleDetailsPersistanceAdapter implements SaleDetailsPersistancePort {

    private final SaleDetailsRepository repository;
    private final SaleDetailsMapper mapper;

    /**
     * Reads a sale detail by its ID
     *
     * @param id the ID of the sale detail
     * @return the SaleDetails object if found
     * @throws SaleDetailNotFound if the sale detail is not found
     */
    @Override
    public SaleDetails readById(Long id) {
        return repository.findById(id)
                .map(mapper::toDomain)
                .orElseThrow(() -> new SaleDetailNotFound("Detalle no encontrado"));
    }

    /**
     * Creates a new sale detail
     *
     * @param request the SaleDetails object to be created
     * @return the created SaleDetails object
     */
    @Override
    public SaleDetails create(SaleDetails request) {
        var entity = mapper.toEntity(request);
        var savedEntity = repository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    /**
     * Updates an existing sale detail
     *
     * @param request the SaleDetails object with updated data
     * @param id the ID of the sale detail to be updated
     * @return the updated SaleDetails object
     * @throws SaleDetailNotFound if the sale detail is not found
     */
    @Override
    public SaleDetails update(SaleDetails request, Long id) {
        return repository.findById(id)
                .map(existingEntity -> {
                    var entityToUpdate = mapper.toEntity(request);
                    entityToUpdate.setId(id);
                    entityToUpdate.setSale(existingEntity.getSale());
                    return mapper.toDomain(repository.save(entityToUpdate));
                })
                .orElseThrow(() -> new SaleDetailNotFound("Detalle no encontrado"));
    }

    /**
     * Deletes a sale detail by its ID
     *
     * @param id the ID of the sale detail to be deleted
     * @throws SaleDetailNotFound if the sale detail is not found
     */
    @Override
    public void deleteById(Long id) {
        repository.findById(id).orElseThrow(
                () -> new SaleDetailNotFound("Detalle no encontrado"));
        repository.deleteById(id);
    }
}
