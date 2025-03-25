package com.tienda.com.tienda.inventoryserver.application.services;

import com.tienda.com.tienda.inventoryserver.application.mapper.StockDomainMapper;
import com.tienda.com.tienda.inventoryserver.application.validation.FeignValidator;
import com.tienda.com.tienda.inventoryserver.domain.model.constant.StockStatus;
import com.tienda.com.tienda.inventoryserver.domain.model.dto.Product;
import com.tienda.com.tienda.inventoryserver.domain.model.dto.Stock;
import com.tienda.com.tienda.inventoryserver.domain.model.dto.request.StockRequest;
import com.tienda.com.tienda.inventoryserver.domain.model.dto.response.StockResponse;
import com.tienda.com.tienda.inventoryserver.domain.port.StockPersistancePort;
import com.tienda.com.tienda.inventoryserver.application.useCases.StockUseCases;
import com.tienda.com.tienda.inventoryserver.infraestructure.adapters.client.ProductFeignClient;
import com.tienda.com.tienda.inventoryserver.infraestructure.adapters.exception.InsufficientStock;
import com.tienda.com.tienda.inventoryserver.infraestructure.adapters.exception.StockNotFoundException;
import com.tienda.com.tienda.inventoryserver.infraestructure.adapters.exception.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.tienda.com.tienda.inventoryserver.application.validation.RequestValidator.validateExpirationAdquisitionDate;

@Service
@AllArgsConstructor
public class StockService implements StockUseCases {

    private final ProductFeignClient productFeignClient;
    private final StockDomainMapper stockDomainMapper;
    private final StockPersistancePort stockPersistancePort;
    private final FeignValidator feignValidator;

    /**
     * Updates the quantity of a product's stock
     *
     * @param productId   ID of the product
     * @param newQuantity New quantity to set
     * @return Updated stock response
     */
    @Override
    public StockResponse updateQuantity(String productId, int newQuantity) {
        var stocks = validateExistingStock(productId);
        var oldestStock = stocks.getFirst();
        oldestStock.setQuantity(newQuantity);
        var updatedStock = stockPersistancePort.update(oldestStock, oldestStock.getId());
        return stockDomainMapper.stockToStockResponse(updatedStock);
    }

    /**
     * Increments the stock quantity of a product
     *
     * @param productId ID of the product
     * @param quantity  Quantity to add
     * @return Updated stock response
     */
    @Override
    public StockResponse incrementQuantity(String productId, int quantity) {
        var stocks = validateExistingStock(productId);
        var oldestStock = stocks.getFirst();
        oldestStock.setQuantity(oldestStock.getQuantity() + quantity);
        var updatedStock = stockPersistancePort.update(oldestStock, oldestStock.getId());
        return stockDomainMapper.stockToStockResponse(updatedStock);
    }

    /**
     * Decreases the stock quantity of a product, ensuring sufficient stock
     *
     * @param productId ID of the product
     * @param quantity  Quantity to remove
     * @param unitPrice Price per unit
     * @return Updated stock response
     */
    @Override
    public StockResponse decrementQuantity(String productId, int quantity, BigDecimal unitPrice) {
        var stocks = validateExistingStock(productId);
        int remainingQuantity = quantity;
        validateProductQuantity(remainingQuantity, productId);
        for (Stock stock : stocks) {
            validateStockSalePolitic(stock, unitPrice);
            if (remainingQuantity <= 0) break;
            if (stock.getQuantity() >= remainingQuantity) {
                stock.setQuantity(stock.getQuantity() - remainingQuantity);
                var updatedStock = stockPersistancePort.updateDecrement(stock, stock.getId());
                return stockDomainMapper.stockToStockResponse(updatedStock);
            } else {
                remainingQuantity -= stock.getQuantity();
                stock.setQuantity(0);
                stockPersistancePort.updateDecrement(stock, stock.getId());
            }
        }
        return stockDomainMapper.stockToStockResponse(stocks.getLast());
    }

    /**
     * Validates that the sale price is within the allowed range.
     *
     * @param stock     The stock item
     * @param unitPrice Price per unit
     */
    private void validateStockSalePolitic(Stock stock, BigDecimal unitPrice) {
        BigDecimal precioMinimo = stock.getPurchaseUnitCost().multiply(BigDecimal.valueOf(0.75));
        BigDecimal precioMaximo = stock.getPurchaseUnitCost().multiply(BigDecimal.valueOf(1.75));
        if (unitPrice.compareTo(precioMinimo) < 0 || unitPrice.compareTo(precioMaximo) > 0) {
            throw new ValidationException("El precio de venta debe estar entre "
                    + precioMinimo + " y " + precioMaximo);
        }
    }

    /**
     * Validates if there is enough stock available.
     *
     * @param remainingQuantity Quantity to validate.
     * @param productId         ID of the product.
     */
    private void validateProductQuantity(int remainingQuantity, String productId) {
        var validStock = getStockWithoutExpiringDate(productId);
        var invalidStock = getExpiredStock(productId).get("Stock expirado");
        var total = validStock + invalidStock;
        if (total < remainingQuantity)
            throw new InsufficientStock("Stock valido insuficiente para el producto con ID: " + productId);
        if (validStock < remainingQuantity)
            throw new InsufficientStock("Existe la cantidad pedida pero parte del pedido esta caducado " + productId);
    }

    /**
     * Retrieves the total stock information for a given product
     *
     * @param productId the ID of the product
     * @return a map containing product name, valid stock, expired stock, and total stock
     */
    @Override
    public Map<String, String> getTotalStock(String productId) {
        var stocks = validateExistingStock(productId);
        Map<String, String> totalStock = new HashMap<>();
        var product = productFeignClient.getProductByCod(stocks.getFirst().getProductId());
        var validStock = getStockWithoutExpiringDate(productId);
        var invalidStock = getExpiredStock(productId).get("Stock expirado");
        totalStock.put("Nombre de producto", product.getName());
        totalStock.put("Stock valido: ", validStock.toString());
        totalStock.put("Stock caducado: ", invalidStock.toString());
        totalStock.put("Total: ", (validStock + invalidStock) +"");
        return totalStock;
    }

    /**
     * Retrieves the oldest stock entry for a given product
     *
     * @param productId the ID of the product
     * @return the oldest stock response
     */
    @Override
    public StockResponse getOldestStock(String productId) {
        var stocks = validateExistingStock(productId);
        return stockDomainMapper.stockToStockResponse(stocks.getFirst());
    }

    /**
     * Retrieves stock entries that will expire within a given number of days
     *
     * @param days the number of days to check for expiration
     * @return a list of expiring stock responses
     */
    @Override
    public List<StockResponse> getExpiringDate(Integer days) {
        LocalDate expiryDateLimit = LocalDate.now().plusDays(days);
        var expiringStocks = stockPersistancePort.findAllStocksByExpiryDateBefore(expiryDateLimit);
        return expiringStocks.stream()
                .map(stockDomainMapper::stockToStockResponse)
                .collect(Collectors.toList());
    }

    /**
     * Checks if any products are below the stock threshold for a given category
     *
     * @param threshold the minimum stock quantity required
     * @param category  the product category to filter (optional)
     * @return a list of products that are below the threshold
     */
    @Override
    public List<Product> checkStockThreshold(Integer threshold, String category) {
        var totalStockByProduct = stockPersistancePort.findAllStocks()
                .stream().collect(Collectors.groupingBy(Stock::getProductId,
                        Collectors.summingInt(Stock::getQuantity)));
        var lowStockProductIds = totalStockByProduct.entrySet().stream()
                .filter(entry -> entry.getValue() < threshold)
                .map(Map.Entry::getKey).toList();
        return lowStockProductIds.stream().map(productFeignClient::getProductByCod)
                .filter(product -> category == null || product.getCategory()
                .getName().equals(category)).distinct().collect(Collectors.toList());
    }

    /**
     * Retrieves all stock entries associated with a specific provider
     *
     * @param providerId the ID of the provider
     * @return a list of stock responses linked to the provider
     */
    @Override
    public List<StockResponse> getStocksByProviderId(UUID providerId) {
        var lowStocks = stockPersistancePort.findAllByProvider(providerId);
        return lowStocks.stream()
                .map(stockDomainMapper::stockToStockResponse)
                .toList();
    }

    /**
     * Retrieves the total expired stock for a given product
     *
     * @param productId the ID of the product
     * @return a map containing the total expired stock
     */
    @Override
    public Map<String, Integer> getExpiredStock(String productId) {
        var stocks = validateExistingStock(productId);
        int totalExpiredStock = stocks.stream()
                .filter(stock -> stock.getExpiryDate() != null && stock.getExpiryDate().isBefore(LocalDate.now()))
                .mapToInt(Stock::getQuantity)
                .sum();
        Map<String, Integer> result = new HashMap<>();
        result.put("Stock expirado", totalExpiredStock);
        return result;
    }

    /**
     * Retrieves the total stock quantity without an expiration date
     *
     * @param productId the ID of the product
     * @return the total quantity of non-expired stock
     */
    @Override
    public Integer getStockWithoutExpiringDate(String productId) {
        var stocks = validateExistingStock(productId);
        return stocks.stream()
                .filter(stock -> stock.getExpiryDate() == null || stock.getExpiryDate().isAfter(LocalDate.now()))
                .mapToInt(Stock::getQuantity)
                .sum();
    }

    /**
     * Retrieves all stock entries within a given date range
     *
     * @param startDate The start date of the range
     * @param endDate   The end date of the range
     * @return A list of stock responses that fall within the specified date range
     */
    @Override
    public List<StockResponse> getAllStocksBetweenDates(LocalDate startDate, LocalDate endDate) {
        var lowStocks = stockPersistancePort.findAllStocksByPurchaseDateBetween(startDate, endDate);
        return lowStocks.stream()
                .map(stockDomainMapper::stockToStockResponse)
                .toList();
    }

    /**
     * Retrieves a stock entry by its unique identifier
     *
     * @param uuid The unique identifier of the stock
     * @return The stock response associated with the given UUID
     */
    @Override
    public StockResponse getById(UUID uuid) {
        var stock = stockPersistancePort.readById(uuid);
        return stockDomainMapper.stockToStockResponse(stock);
    }

    /**
     * Creates a new stock entity based on the given request
     *
     * @param request The stock request containing details for the new entity
     * @return The created stock response
     */
    @Override
    public StockResponse createEntity(StockRequest request) {
        var stock = stockDomainMapper.stockRequestToStock(request);
        stock.setId(UUID.randomUUID());
        stock.setTotalPurchaseCost(stock.getPurchaseUnitCost().multiply(new BigDecimal(stock.getQuantity())));
        feignValidator.verifyExistingProduct(stock.getProductId());
        feignValidator.verifyExistingProvider(stock.getProviderId());
        if (request.getExpiryDate() != null)
            validateExpirationAdquisitionDate(stock.getPurchaseDate(), stock.getExpiryDate());
        var savedStock = stockPersistancePort.create(stock);
        return stockDomainMapper.stockToStockResponse(savedStock);
    }

    /**
     * Updates an existing stock entity
     *
     * @param request The updated stock request details
     * @param uuid    The unique identifier of the stock to update
     * @return The updated stock response
     */
    @Override
    public StockResponse updateEntity(StockRequest request, UUID uuid) {
        var updatedStock = stockDomainMapper.stockRequestToStock(request);
        var savedStock = stockPersistancePort.update(updatedStock, uuid);
        return stockDomainMapper.stockToStockResponse(savedStock);
    }

    /**
     * Deletes a stock entity by its unique identifier
     *
     * @param uuid The unique identifier of the stock to delete
     */
    @Override
    public void deleteEntityById(UUID uuid) {
        stockPersistancePort.deleteById(uuid);
    }

    /**
     * Retrieves all stock entries
     *
     * @return A list of all stock responses
     */
    @Override
    public List<StockResponse> getAllStocks() {
        return stockPersistancePort
                .findAllStocks()
                .stream()
                .map(stockDomainMapper::stockToStockResponse)
                .toList();
    }

    /**
     * Retrieves the inventory status based on stock levels and threshold values
     *
     * @param lowStockThresholds A map containing product IDs and their respective low-stock thresholds
     * @return A map of product IDs and their corresponding stock status
     */
    @Override
    public Map<String, StockStatus> getInventoryStatus(Map<String, Integer> lowStockThresholds) {
        var currentStock = getProductStockStatus();
        Map<String, StockStatus> inventoryStatus = new HashMap<>();
        for (Map.Entry<String, Integer> entry : currentStock.entrySet()) {
            String productId = entry.getKey();
            Integer stock = entry.getValue();
            if (stock <= 0) {
                inventoryStatus.put(productId, StockStatus.OUT_OF_STOCK);
            } else if (lowStockThresholds.containsKey(productId) && stock <= lowStockThresholds.get(productId)) {
                inventoryStatus.put(productId, StockStatus.LOW_STOCK);
            } else inventoryStatus.put(productId, StockStatus.IN_STOCK);
        }
        return inventoryStatus;
    }

    /**
     * Retrieves the stock count for each product
     *
     * @return A map of product IDs and their corresponding stock count
     */
    public Map<String, Integer> getProductStockStatus() {
        var stocks = stockPersistancePort.findAllStocks();
        return stocks.stream()
                .collect(Collectors.groupingBy(
                        Stock::getProductId,
                        Collectors.summingInt(stock -> getStockWithoutExpiringDate(stock.getProductId()))
                ));
    }

    /**
     * Validates and retrieves existing stock for a given product
     *
     * @param productId The ID of the product to check stock for
     * @return A list of stock entries for the specified product
     * @throws StockNotFoundException If no stock is found for the product
     */
    public List<Stock> validateExistingStock(String productId) {
        var stocks = stockPersistancePort.findAllStocksByProductId(productId);
        if (stocks.isEmpty())
            throw new StockNotFoundException("No se encontro stock para el producto con ID: " + productId);
        return stocks;
    }
}
