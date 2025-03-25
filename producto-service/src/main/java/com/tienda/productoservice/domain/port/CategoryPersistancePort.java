package com.tienda.productoservice.domain.port;

import com.tienda.productoservice.domain.abstractions.PersistancePort;
import com.tienda.productoservice.domain.model.dto.Category;

import java.util.List;

public interface CategoryPersistancePort extends PersistancePort<Category, Long> {
    Category findCategoryByName(String brand);
    List<Category> findAllCategories();
}
