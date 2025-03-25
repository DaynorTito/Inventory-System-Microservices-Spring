package com.tienda.productoservice.domain.abstractions;

public interface PersistancePort <E,ID>{

    E readById(ID id);
    E create(E request);
    E update(E request, ID id);
    void deleteById(ID id);
}
