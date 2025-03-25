package com.tienda.salieservice.domain.port;

import com.tienda.salieservice.domain.abstractions.PersistancePort;
import com.tienda.salieservice.domain.model.dto.SaleDetails;

public interface SaleDetailsPersistancePort extends PersistancePort<SaleDetails, Long> {
}
