package com.tienda.com.tienda.inventoryserver.application.services;


import com.tienda.com.tienda.inventoryserver.application.mapper.KardexDomainMapper;
import com.tienda.com.tienda.inventoryserver.application.mapper.StockDomainMapper;
import com.tienda.com.tienda.inventoryserver.application.useCases.TransactionsStockIO;
import com.tienda.com.tienda.inventoryserver.domain.model.constant.TypeMove;
import com.tienda.com.tienda.inventoryserver.domain.model.dto.request.PurchaseInventoryRequest;
import com.tienda.com.tienda.inventoryserver.domain.model.dto.request.SaleInventoryRequest;
import com.tienda.com.tienda.inventoryserver.infraestructure.adapters.exception.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@AllArgsConstructor
public class ManagementInventory implements TransactionsStockIO {

    private final KardexService kardexService;
    private final StockService stockService;
    private final StockDomainMapper stockMapper;
    private final KardexDomainMapper kardexMapper;

    /**
     * Register a purchase from Purchase service
     *
     * @param request body that contains all information about purchase
     * @return a message confirmation
     */
    public String registerInputInventory(PurchaseInventoryRequest request) {
        if (request.getExpiryDate() != null && request.getExpiryDate().isBefore(LocalDate.now().plusDays(3)))
            throw new ValidationException("La fecha de vencimiento esta demasiado proxima, minimo 3 dias de diferencia");
        var requestStock = stockMapper.registerPurchase(request);
        requestStock.setPurchaseDate(LocalDate.now());
        var kardexRequest = kardexMapper.createPurchaseKardex(request);
        kardexRequest.setTypeMovement(TypeMove.INCOME);
        kardexRequest.setMovementDate(LocalDate.now());
        stockService.createEntity(requestStock);
        kardexService.createEntity(kardexRequest);
        return "Registro de compra creado exitosamente";
    }

    /**
     * Register a sale from Sale service
     *
     * @param request body that contains all information of a sale
     * @return message of operation confirmation
     */
    public String registerOutputInventory(SaleInventoryRequest request) {
        var requestStock = kardexMapper.createSaleKardex(request);
        requestStock.setTypeMovement(TypeMove.OUTCOME);
        requestStock.setMovementDate(LocalDate.now());
        stockService.decrementQuantity(requestStock.getProductId(), request.getQuantity(), request.getUnitPrice());
        kardexService.createEntity(requestStock);
        return "Registro de compra creado exitosamente";
    }
}
