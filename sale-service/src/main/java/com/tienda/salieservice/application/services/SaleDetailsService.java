package com.tienda.salieservice.application.services;


import com.tienda.salieservice.application.mapper.SaleDetailsDomainMapper;
import com.tienda.salieservice.domain.model.dto.request.SaleDetailsRequest;
import com.tienda.salieservice.domain.model.dto.response.SaleDetailsResponse;
import com.tienda.salieservice.domain.port.SaleDetailsPersistancePort;
import com.tienda.salieservice.application.useCases.SaleDetailsUseCases;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SaleDetailsService implements SaleDetailsUseCases {

    private final SaleDetailsPersistancePort saleDetailsPersistancePort;
    private final SaleDetailsDomainMapper saleDetailsDomainMapper;

    /**
     * Retrieves a SaleDetail by its ID
     *
     * @param aLong The ID of the SaleDetail to retrieve
     * @return A SaleDetailsResponse object containing the details of the SaleDetail
     */
    @Override
    public SaleDetailsResponse getById(Long aLong) {
        var detail = saleDetailsPersistancePort.readById(aLong);
        return saleDetailsDomainMapper.saleDetailsToSaleDetailsResponse(detail);
    }

    /**
     * Creates a new SaleDetail entity
     *
     * @param request A SaleDetailsRequest object containing the information to create the SaleDetail
     * @return A SaleDetailsResponse object containing the details of the newly created SaleDetail
     */
    @Override
    public SaleDetailsResponse createEntity(SaleDetailsRequest request) {
        var detail = saleDetailsDomainMapper.saleDetailsRequestToSaleDetails(request);
        var savedDetail = saleDetailsPersistancePort.create(detail);
        return saleDetailsDomainMapper.saleDetailsToSaleDetailsResponse(savedDetail);
    }

    /**
     * Updates an existing SaleDetail entity
     *
     * @param request A SaleDetailsRequest object containing the updated information for the SaleDetail
     * @param aLong The ID of the SaleDetail to update
     * @return A SaleDetailsResponse object containing the updated details of the SaleDetail
     */
    @Override
    public SaleDetailsResponse updateEntity(SaleDetailsRequest request, Long aLong) {
        var detail = saleDetailsDomainMapper.saleDetailsRequestToSaleDetails(request);
        var updatedDetail = saleDetailsPersistancePort.update(detail, aLong);
        return saleDetailsDomainMapper.saleDetailsToSaleDetailsResponse(updatedDetail);
    }

    /**
     * Deletes a SaleDetail entity by its ID
     *
     * @param aLong The ID of the SaleDetail to delete
     */
    @Override
    public void deleteEntityById(Long aLong) {
        saleDetailsPersistancePort.deleteById(aLong);
    }
}
