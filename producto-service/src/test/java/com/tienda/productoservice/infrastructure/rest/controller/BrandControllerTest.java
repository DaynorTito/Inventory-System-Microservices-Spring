package com.tienda.productoservice.infrastructure.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tienda.productoservice.application.services.BrandService;
import com.tienda.productoservice.domain.model.dto.request.BrandRequest;
import com.tienda.productoservice.domain.model.dto.response.BrandResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BrandControllerTest {
    private MockMvc mockMvc;

    @Mock
    private BrandService brandService;

    @InjectMocks
    private BrandController brandController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(brandController).build();
    }

    @Test
    void getBrands_ShouldReturnListOfBrands() throws Exception {
        List<BrandResponse> brands = List.of(new BrandResponse(1L, "Nike", "Deportes"));
        when(brandService.getAllBrands()).thenReturn(brands);

        mockMvc.perform(get("/brand"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].name").value("Nike"));
    }

    @Test
    void getBrandById_ShouldReturnBrand() throws Exception {
        BrandResponse brand = new BrandResponse(1L, "Adidas", "Ropa deportiva");
        when(brandService.getById(1L)).thenReturn(brand);

        mockMvc.perform(get("/brand/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Adidas"));
    }

    @Test
    void createBrand_ShouldReturnCreatedBrand() throws Exception {
        BrandRequest request = new BrandRequest("Puma", "Calzado");
        BrandResponse response = new BrandResponse(2L, "Puma", "Calzado");
        when(brandService.createEntity(any())).thenReturn(response);

        mockMvc.perform(post("/brand")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Puma"));
    }

    @Test
    void updateBrand_ShouldReturnUpdatedBrand() throws Exception {
        BrandRequest request = new BrandRequest("Reebok", "Deportivo");
        BrandResponse response = new BrandResponse(3L, "Reebok", "Deportivo");
        when(brandService.updateEntity(any(), any())).thenReturn(response);

        mockMvc.perform(put("/brand/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Reebok"));
    }

    @Test
    void deleteBrand_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/brand/4"))
                .andExpect(status().isNoContent());
    }
}