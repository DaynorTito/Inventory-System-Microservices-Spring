package com.tienda.com.tienda.inventoryserver.infraestructure.adapters.adapterimpl;

import com.tienda.com.tienda.inventoryserver.domain.model.dto.Stock;
import com.tienda.com.tienda.inventoryserver.domain.port.StockPersistancePort;
import com.tienda.com.tienda.inventoryserver.infraestructure.adapters.exception.StockNotFoundException;
import com.tienda.com.tienda.inventoryserver.infraestructure.adapters.mapper.StockMapper;
import com.tienda.com.tienda.inventoryserver.infraestructure.adapters.repository.StockRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class StockPersistanceAdapter implements StockPersistancePort {

    private final StockRepository stockRepository;
    private final StockMapper stockMapper;

    /**
     * Method to get all stock records by product ID
     *
     * @param productId Product ID
     * @return List of Stock objects for the given product
     */
    @Override
    public List<Stock> findAllStocksByProductId(String productId) {
        var stockEntities = stockRepository.findAllByProductIdOrderByExpiryDateAsc(productId);
        return stockEntities.stream().map(stockMapper::toDomain).toList();
    }

    /**
     * Method to get all stock records with an expiry date before the given date
     *
     * @param expiryDate Expiry date to compare
     * @return List of Stock objects with expiry dates before the given date
     */
    @Override
    public List<Stock> findAllStocksByExpiryDateBefore(LocalDate expiryDate) {
        var stockEntities = stockRepository.findAllByExpiryDateBefore(expiryDate);
        return stockEntities.stream().map(stockMapper::toDomain).toList();
    }

    /**
     * Method to get all stock records by provider ID
     *
     * @param uuid Provider ID
     * @return List of Stock objects for the given provider
     */
    @Override
    public List<Stock> findAllByProvider(UUID uuid) {
        var stockEntities = stockRepository.findAllByProviderIdOrderByExpiryDateAsc(uuid);
        return stockEntities.stream().map(stockMapper::toDomain).toList();
    }

    /**
     * Method to get all stock records
     *
     * @return List of all Stock objects
     */
    @Override
    public List<Stock> findAllStocks() {
        var stocks = stockRepository.findAll();
        return stocks.stream().map(stockMapper::toDomain).toList();
    }

    /**
     * Method to get stock records between purchase dates
     *
     * @param after Start date of the range
     * @param before End date of the range
     * @return List of Stock objects within the purchase date range
     */
    @Override
    public List<Stock> findAllStocksByPurchaseDateBetween(LocalDate after, LocalDate before) {
        var stockEntities = stockRepository.findAllByPurchaseDateBetween(after, before);
        return stockEntities.stream().map(stockMapper::toDomain).toList();
    }

    /**
     * Method to get stock records with a quantity less than the given value
     *
     * @param quantity Maximum quantity to compare
     * @return List of Stock objects with quantity less than the given value
     */
    @Override
    public List<Stock> findAllStocksByQuantityLessThan(Integer quantity) {
        var stockEntities = stockRepository.findAllByQuantityLessThan(quantity);
        return stockEntities.stream().map(stockMapper::toDomain).toList();
    }

    /**
     * Method to get a stock record by its UUID
     *
     * @param uuid Stock UUID
     * @return Stock object with the given UUID
     */
    @Override
    public Stock readById(UUID uuid) {
        var stockEntity = stockRepository.findById(uuid).orElseThrow(
                () -> new StockNotFoundException("Stock not found"));
        return stockMapper.toDomain(stockEntity);
    }

    /**
     * Method to create a new stock record
     *
     * @param request Stock request object
     * @return Created Stock object
     */
    @Override
    public Stock create(Stock request) {
        var stockEntity = stockMapper.toEntity(request);
        return stockMapper.toDomain(stockRepository.save(stockEntity));
    }

    /**
     * Method to update an existing stock record
     *
     * @param request Stock request object
     * @param uuid Stock UUID
     * @return Updated Stock object
     */
    @Override
    public Stock update(Stock request, UUID uuid) {
        var stockToUpdate = stockRepository.findById(uuid).orElseThrow(
                () -> new RuntimeException("Stock not found"));
        var stockEntity = stockMapper.toEntity(request);
        UpdateHelper.updateNonNullFields(stockEntity, stockToUpdate);
        if (request.getQuantity() != null || request.getPurchaseUnitCost() != null)
            stockToUpdate.setTotalPurchaseCost(stockToUpdate.getPurchaseUnitCost()
                    .multiply(new BigDecimal(stockToUpdate.getQuantity())));
        return stockMapper.toDomain(stockRepository.save(stockToUpdate));
    }

    /**
     * Method to update stock by decrementing the quantity
     *
     * @param request Stock request object
     * @param uuid Stock UUID
     * @return Updated Stock object
     */
    @Override
    public Stock updateDecrement(Stock request, UUID uuid) {
        var stockToUpdate = stockRepository.findById(uuid).orElseThrow(
                () -> new RuntimeException("Stock not found"));
        var stockEntity = stockMapper.toEntity(request);
        UpdateHelper.updateNonNullFields(stockEntity, stockToUpdate);
        return stockMapper.toDomain(stockRepository.save(stockToUpdate));
    }

    /**
     * Method to delete a stock record by its UUID
     *
     * @param uuid Stock UUID
     */
    @Override
    public void deleteById(UUID uuid) {
        var stockEntity = stockRepository.findById(uuid).orElseThrow(
                () -> new RuntimeException("Stock not found"));
        stockRepository.deleteById(stockEntity.getId());
    }
}
