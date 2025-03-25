package com.tienda.compraservice.infraestructure.adapters.adapterimpl;

import com.tienda.compraservice.domain.model.dto.Provider;
import com.tienda.compraservice.infraestructure.adapters.entity.ProviderEntity;
import com.tienda.compraservice.infraestructure.adapters.repository.ProviderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class UpdateHelperTest {

    @Test
    void updateNonNullFields_ShouldUpdateProviderEntityFieldsWhenNotNull() {
        MockitoAnnotations.openMocks(this);
        Provider mockProvider = Provider.builder()
                .id(UUID.randomUUID())
                .name("Provider A")
                .address("Address A")
                .phone("123456789")
                .email("providerA@example.com")
                .active(true)
                .build();

        ProviderEntity mockProviderEntity = new ProviderEntity();
        mockProviderEntity.setId(UUID.randomUUID());
        mockProviderEntity.setName("Old Provider");
        mockProviderEntity.setAddress("Old Address");
        mockProviderEntity.setPhone("000000000");
        mockProviderEntity.setEmail("old@example.com");
        mockProviderEntity.setActive(false);
        mockProvider.setName("Updated Provider");
        mockProvider.setAddress("Updated Address");
        UpdateHelper.updateNonNullFields(mockProvider, mockProviderEntity);
        assertEquals("Updated Provider", mockProviderEntity.getName());
        assertEquals("Updated Address", mockProviderEntity.getAddress());
    }

}