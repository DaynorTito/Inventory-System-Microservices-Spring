package com.tienda.com.tienda.inventoryserver.domain.abstraction;

public interface PersistancePort <E,ID>{
    E readById(ID id);
    E create(E request);
    E update(E request, ID id);
    void deleteById(ID id);
}
