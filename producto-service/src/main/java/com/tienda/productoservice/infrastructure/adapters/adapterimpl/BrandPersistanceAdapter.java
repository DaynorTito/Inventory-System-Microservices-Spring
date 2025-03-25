package com.tienda.productoservice.infrastructure.adapters.adapterimpl;


import com.tienda.productoservice.domain.model.dto.Brand;
import com.tienda.productoservice.domain.port.BrandPersistancePort;
import com.tienda.productoservice.infrastructure.adapters.exception.BrandNotFoundException;
import com.tienda.productoservice.infrastructure.adapters.mapper.BrandMapper;
import com.tienda.productoservice.infrastructure.adapters.repository.BrandRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class BrandPersistanceAdapter implements BrandPersistancePort {

    private final BrandRepository brandRepository;
    private final BrandMapper brandMapper;

    /**
     * Get a brand by its ID
     *
     * @param id the ID of the brand to retrieve
     * @return the found brand, or null if not found
     */
    @Override
    public Brand readById(Long id) {
        var brand = brandRepository.findById(id);
        return brand.map(brandMapper::toDomain).orElse(null);
    }

    /**
     * Create a new brand in the database
     *
     * @param request the brand to create
     * @return the created brand
     */
    @Override
    public Brand create(Brand request) {
        var brandToSave = brandMapper.toEntity(request);
        var brandSaved = brandRepository.save(brandToSave);
        return brandMapper.toDomain(brandSaved);
    }

    /**
     * Update an existing brand in the database
     *
     * @param request the new information for the brand
     * @param id the ID of the brand to update
     * @return the updated brand
     */
    @Override
    public Brand update(Brand request, Long id) {
        var existingBrand = brandRepository.findById(id)
                .orElseThrow(() -> new BrandNotFoundException("Marca no encontrada"));
        var requestEntity = brandMapper.toEntity(request);
        UpdateHelper.updateNonNullFields(requestEntity, existingBrand);
        var brandUpdated = brandRepository.save(existingBrand);
        return brandMapper.toDomain(brandUpdated);
    }

    /**
     * Delete a brand from the database by its ID
     *
     * @param id the ID of the brand to delete
     */
    @Override
    public void deleteById(Long id) {
        brandRepository.deleteById(id);
    }

    /**
     * Find a brand by its name
     *
     * @param brand the name of the brand to search for
     * @return the found brand, or null if not found
     */
    @Override
    public Brand findBrandByName(String brand) {
        var brandFind = brandRepository.findByName(brand);
        return brandFind.map(brandMapper::toDomain).orElse(null);
    }

    /**
     * Get all brands from the database
     *
     * @return a list of all brands
     */
    @Override
    public List<Brand> findAllBrands() {
        return brandRepository.findAll().stream()
                .map(brandMapper::toDomain)
                .toList();
    }
}
