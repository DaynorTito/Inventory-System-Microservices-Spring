package com.tienda.com.tienda.inventoryserver.domain.abstraction;

public interface CrudService<RQ, RS, ID> {
    RS getById(ID id);
    RS createEntity(RQ request);
    RS updateEntity(RQ request, ID id);
    void deleteEntityById(ID id);
}
