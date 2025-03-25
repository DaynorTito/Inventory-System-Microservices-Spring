package com.tienda.compraservice.infraestructure.adapters.adapterimpl;


import com.tienda.compraservice.domain.model.dto.Provider;
import com.tienda.compraservice.infraestructure.adapters.entity.ProviderEntity;
import com.tienda.compraservice.infraestructure.adapters.exception.ProviderNotFoundException;
import com.tienda.compraservice.infraestructure.adapters.mapper.ProviderMapper;
import com.tienda.compraservice.infraestructure.adapters.repository.ProviderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProviderPersistanceAdapterTest {

    @Mock
    private ProviderRepository providerRepository;

    @Mock
    private ProviderMapper providerMapper;

    @InjectMocks
    private ProviderPersistanceAdapter providerPersistanceAdapter;

    private Provider provider;
    private ProviderEntity providerEntity;
    private UUID providerId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        providerId = UUID.randomUUID();
        provider = Provider.builder()
                .id(providerId)
                .name("ProviderName")
                .address("Address")
                .phone("123456")
                .email("email@example.com")
                .active(true)
                .build();

        providerEntity = ProviderEntity.builder()
                .id(providerId)
                .name("ProviderName")
                .address("Address")
                .phone("123456")
                .email("email@example.com")
                .active(true)
                .build();
    }

    @Test
    void getAllProviders_ShouldReturnList() {
        List<ProviderEntity> providerList = List.of(providerEntity);
        Mockito.when(providerRepository.findAll()).thenReturn(providerList);
        Mockito.when(providerMapper.toDomain(providerEntity)).thenReturn(provider);
        var result = providerPersistanceAdapter.findAllProviders();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("ProviderName", result.getFirst().getName());
    }

    @Test
    void getById_ShouldReturnProvider() {
        Mockito.when(providerRepository.findById(providerId)).thenReturn(Optional.of(providerEntity));
        Mockito.when(providerMapper.toDomain(providerEntity)).thenReturn(provider);
        var result = providerPersistanceAdapter.readById(providerId);
        assertNotNull(result);
        assertEquals(providerId, result.getId());
        assertEquals("ProviderName", result.getName());
    }

    @Test
    void getById_ShouldReturnNullWhenNotFound() {
        Mockito.when(providerRepository.findById(providerId)).thenReturn(Optional.empty());
        var result = providerPersistanceAdapter.readById(providerId);
        assertNull(result);
    }

    @Test
    void getByName_ShouldReturnProvider() {
        Mockito.when(providerRepository.findByName("ProviderName")).thenReturn(Optional.of(providerEntity));
        Mockito.when(providerMapper.toDomain(providerEntity)).thenReturn(provider);
        var result = providerPersistanceAdapter.findProviderByName("ProviderName");
        assertNotNull(result);
        assertEquals("ProviderName", result.getName());
    }

    @Test
    void getByName_ShouldReturnNullWhenNotFound() {
        Mockito.when(providerRepository.findByName("NonExistingProvider")).thenReturn(Optional.empty());
        var result = providerPersistanceAdapter.findProviderByName("NonExistingProvider");
        assertNull(result);
    }

    @Test
    void create_ShouldReturnCreatedProvider() {
        ProviderEntity providerToSave = ProviderEntity.builder()
                .id(providerId)
                .name("ProviderName")
                .address("Address")
                .phone("123456")
                .email("email@example.com")
                .active(true)
                .build();
        Mockito.when(providerMapper.toEntity(provider)).thenReturn(providerToSave);
        Mockito.when(providerRepository.save(providerToSave)).thenReturn(providerToSave);
        Mockito.when(providerMapper.toDomain(providerToSave)).thenReturn(provider);
        var result = providerPersistanceAdapter.create(provider);
        assertNotNull(result);
        assertEquals("ProviderName", result.getName());
    }

    @Test
    void update_ShouldReturnUpdatedProvider() {
        ProviderEntity providerToUpdate = ProviderEntity.builder()
                .id(providerId)
                .name("ProviderName")
                .address("Address")
                .phone("123456")
                .email("email@example.com")
                .active(true)
                .build();

        ProviderEntity updatedProviderEntity = ProviderEntity.builder()
                .id(providerId)
                .name("UpdatedProvider")
                .address("New Address")
                .phone("987654")
                .email("newemail@example.com")
                .active(true)
                .build();

        Mockito.when(providerRepository.findById(providerId)).thenReturn(Optional.of(providerToUpdate));
        Mockito.when(providerMapper.toEntity(provider)).thenReturn(updatedProviderEntity);
        Mockito.when(providerRepository.save(updatedProviderEntity)).thenReturn(updatedProviderEntity);
        Mockito.when(providerMapper.toDomain(updatedProviderEntity)).thenReturn(
                Provider.builder()
                        .id(providerId)
                        .name("UpdatedProvider")
                        .address("New Address")
                        .phone("987654")
                        .email("newemail@example.com")
                        .active(true)
                        .build()
        );
        var result = providerPersistanceAdapter.update(provider, providerId);
        assertNotNull(result);
        assertEquals("UpdatedProvider", result.getName());
        assertEquals("New Address", result.getAddress());
    }

    @Test
    void update_ShouldThrowProviderNotFoundException() {
        Mockito.when(providerRepository.findById(providerId)).thenReturn(Optional.empty());
        assertThrows(ProviderNotFoundException.class, () -> providerPersistanceAdapter.update(provider, providerId));
    }

    @Test
    void delete_ShouldDeleteProvider() {
        Mockito.doNothing().when(providerRepository).deleteById(providerId);
        providerPersistanceAdapter.deleteById(providerId);
        Mockito.verify(providerRepository, Mockito.times(1)).deleteById(providerId);
    }
}
