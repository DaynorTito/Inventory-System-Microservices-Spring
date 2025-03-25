package com.tienda.com.tienda.inventoryserver.infraestructure.rest.controller;

import com.tienda.com.tienda.inventoryserver.application.services.ManagementInventory;
import com.tienda.com.tienda.inventoryserver.domain.model.dto.request.PurchaseInventoryRequest;
import com.tienda.com.tienda.inventoryserver.domain.model.dto.request.SaleInventoryRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@ExtendWith(MockitoExtension.class)
class MovementsControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ManagementInventory managementInventory;

    @InjectMocks
    private MovementsController movementsController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(movementsController).build();
    }

    @Test
    void registerInputInventory_ShouldReturnSuccess() throws Exception {
        PurchaseInventoryRequest request = new PurchaseInventoryRequest();
        String responseMessage = "Purchase registered successfully";
        when(managementInventory.registerInputInventory(any(PurchaseInventoryRequest.class)))
                .thenReturn(responseMessage);
        mockMvc.perform(post("/inventory/register-purchase")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(content().string(responseMessage));

        verify(managementInventory, times(1)).registerInputInventory(any(PurchaseInventoryRequest.class));
    }

    @Test
    void registerOutputInventory_ShouldReturnSuccess() throws Exception {
        SaleInventoryRequest request = new SaleInventoryRequest();
        String responseMessage = "Sale registered successfully";
        when(managementInventory.registerOutputInventory(any(SaleInventoryRequest.class)))
                .thenReturn(responseMessage);
        mockMvc.perform(post("/inventory/register-sale")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(content().string(responseMessage));

        verify(managementInventory, times(1)).registerOutputInventory(any(SaleInventoryRequest.class));
    }
}
