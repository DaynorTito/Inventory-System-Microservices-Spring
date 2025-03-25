package com.tienda.salieservice.application.useCases;

import com.tienda.salieservice.domain.abstractions.CrudService;
import com.tienda.salieservice.domain.model.dto.Sale;
import com.tienda.salieservice.domain.model.dto.request.SaleRequest;
import com.tienda.salieservice.domain.model.dto.response.SaleResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface SaleUseCases extends CrudService<SaleRequest, SaleResponse, UUID> {
    List<Sale> findAllSalesBetweenDates(LocalDate startDate, LocalDate endDate);
    List<Sale> findAllSalesByProductId(String productId);
    List<Sale> findAllSalesByProductId(String productId,
                                       LocalDate startDate, LocalDate endDate);
    List<Sale> findAllSalesByCustomer(String customerName);
    List<Sale> findAllSalesByCustomer(String customerName, LocalDate startDate, LocalDate endDate);
    List<Sale> findAllSalesGreatPriceThan(Double price);
    List<Sale> findAllSalesGreatPriceLess(Double price);
}
