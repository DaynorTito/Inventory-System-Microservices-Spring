package com.tienda.productoservice.application.useCases;

import com.tienda.productoservice.domain.abstractions.CrudService;
import com.tienda.productoservice.domain.model.dto.request.CategoryRequest;
import com.tienda.productoservice.domain.model.dto.response.CategoryResponse;

public interface CategoryUseCases extends CrudService<CategoryRequest, CategoryResponse, Long> {
}
