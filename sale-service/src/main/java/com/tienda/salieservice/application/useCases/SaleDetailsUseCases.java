package com.tienda.salieservice.application.useCases;

import com.tienda.salieservice.domain.abstractions.CrudService;
import com.tienda.salieservice.domain.model.dto.request.SaleDetailsRequest;
import com.tienda.salieservice.domain.model.dto.response.SaleDetailsResponse;

public interface SaleDetailsUseCases extends CrudService<SaleDetailsRequest, SaleDetailsResponse, Long> {
}
