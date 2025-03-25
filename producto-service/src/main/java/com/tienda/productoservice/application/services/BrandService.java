package com.tienda.productoservice.application.services;


import com.tienda.productoservice.application.mapper.BrandDomainMapper;
import com.tienda.productoservice.domain.model.dto.Brand;
import com.tienda.productoservice.domain.model.dto.request.BrandRequest;
import com.tienda.productoservice.domain.model.dto.response.BrandResponse;
import com.tienda.productoservice.domain.port.BrandPersistancePort;
import com.tienda.productoservice.application.useCases.BrandUseCases;
import com.tienda.productoservice.infrastructure.adapters.exception.BrandNotFoundException;
import com.tienda.productoservice.infrastructure.adapters.exception.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BrandService implements BrandUseCases {

    private final BrandPersistancePort brandPersistancePort;
    private final BrandDomainMapper brandMapper;

    /**
     * Retrieves all brands from the persistence layer and maps them to BrandResponse DTOs
     *
     * @return a list of all BrandResponse DTOs
     */
    public List<BrandResponse> getAllBrands() {
        var brands = brandPersistancePort.findAllBrands();
        return brands.stream()
                .map(brandMapper::domaintoResponse)
                .toList();
    }

    /**
     * Retrieves a brand by its ID, validates its existence, and maps it to a BrandResponse DTO
     *
     * @param id the ID of the brand to retrieve
     * @return the mapped BrandResponse DTO for the brand
     */
    @Override
    public BrandResponse getById(Long id) {
        var brand = validateExistBrand(id);
        return brandMapper.domaintoResponse(brand);
    }

    /**
     * Creates a new brand based on the provided BrandRequest
     *
     * @param request the BrandRequest DTO containing the data for the new brand
     * @return the mapped BrandResponse DTO for the created brand
     */
    @Override
    public BrandResponse createEntity(BrandRequest request) {
        validateExistBrand(request.getName());
        var brandCreated = brandPersistancePort.create(brandMapper.reqToDomain(request));
        return brandMapper.domaintoResponse(brandCreated);
    }

    /**
     * Updates an existing brand based on the provided BrandRequest and ID
     *
     * @param request the BrandRequest DTO containing the updated data for the brand.
     * @param id the ID of the brand to update.
     * @return the mapped BrandResponse DTO for the updated brand.
     */
    @Override
    public BrandResponse updateEntity(BrandRequest request, Long id) {
        validateExistBrand(id);
        var brandUpdated = brandPersistancePort.update(brandMapper.reqToDomain(request), id);
        return brandMapper.domaintoResponse(brandUpdated);
    }

    /**
     * Deletes a brand by its ID after validating that the brand exists
     *
     * @param id the ID of the brand to delete.
     */
    @Override
    public void deleteEntityById(Long id) {
        validateExistBrand(id);
        brandPersistancePort.deleteById(id);
    }

    /**
     * Validates that a brand exists by checking its ID. Throws an exception if the brand is not found
     *
     * @param id the ID of the brand to validate
     * @return the brand if found
     * @throws BrandNotFoundException if the brand with the given ID is not found
     */
    public Brand validateExistBrand(Long id) {
        var brand = brandPersistancePort.readById(id);
        if (brand == null) {
            throw new BrandNotFoundException("Marca no encontrada");
        }
        return brand;
    }

    /**
     * Validates that a brand does not already exist by checking its name
     * Throws an exception if the brand with the given name already exists
     *
     * @param name the name of the brand to validate.
     * @throws ValidationException if a brand with the given name already exists
     */
    public void validateExistBrand(String name) {
        var brand = brandPersistancePort.findBrandByName(name.trim().toLowerCase());
        if (brand != null) {
            throw new ValidationException("Marca ya existe");
        }
    }
}
