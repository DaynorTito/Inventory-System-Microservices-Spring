package com.tienda.productoservice.domain.port;

import com.tienda.productoservice.domain.abstractions.PersistancePort;
import com.tienda.productoservice.domain.model.dto.Brand;

import java.util.List;

public interface BrandPersistancePort extends PersistancePort<Brand, Long> {
    Brand findBrandByName(String brand);
    List<Brand> findAllBrands();
}
