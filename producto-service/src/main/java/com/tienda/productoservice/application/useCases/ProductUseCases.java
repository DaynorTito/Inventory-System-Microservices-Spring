package com.tienda.productoservice.application.useCases;

import com.tienda.productoservice.domain.abstractions.CrudService;
import com.tienda.productoservice.domain.model.dto.request.ProductRequest;
import com.tienda.productoservice.domain.model.dto.response.ProductResponse;

import java.math.BigDecimal;
import java.util.List;

public interface ProductUseCases extends CrudService<ProductRequest, ProductResponse, String> {
    String createProductCod(ProductRequest productRequest);
    List<ProductResponse> findAllByCategoryBrandAndPrice(String category, String brand, BigDecimal min, BigDecimal max);
    List<ProductResponse> findAllByWord(String word);
}
