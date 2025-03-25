package com.tienda.productoservice.domain.port;

import com.tienda.productoservice.domain.abstractions.PersistancePort;
import com.tienda.productoservice.domain.model.dto.Product;

import java.util.List;

public interface ProductPersistancePort extends PersistancePort<Product, String> {
    List<Product> getAllProducts();
    List<Product> findAllByCategoryAndBrand(String category, String brand);
    List<Product> findAllByWord(String word);
}
