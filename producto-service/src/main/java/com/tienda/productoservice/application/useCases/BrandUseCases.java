package com.tienda.productoservice.application.useCases;

import com.tienda.productoservice.domain.abstractions.CrudService;
import com.tienda.productoservice.domain.model.dto.request.BrandRequest;
import com.tienda.productoservice.domain.model.dto.response.BrandResponse;

public interface BrandUseCases extends CrudService<BrandRequest, BrandResponse, Long> {
}
