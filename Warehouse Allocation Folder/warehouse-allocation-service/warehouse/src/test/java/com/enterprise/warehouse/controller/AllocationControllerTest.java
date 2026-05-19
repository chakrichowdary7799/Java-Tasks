package com.enterprise.warehouse.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import com.enterprise.warehouse.dto.AllocationRequest;
import com.enterprise.warehouse.dto.AllocationResponse;
import com.enterprise.warehouse.service.AllocationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;

@WebMvcTest(AllocationController.class)
public class AllocationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AllocationService allocationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testAllocateEndpoint() throws Exception { // <-- Ensured "throws Exception" is present
        AllocationRequest request = new AllocationRequest();
        request.setProductId(1L);
        request.setQuantity(5);

        AllocationResponse response = AllocationResponse.builder()
                .allocationId(1L)
                .productId(1L)
                .warehouseId(2L)
                .quantity(5)
                .status("SUCCESS")
                .timestamp(LocalDateTime.now())
                .build();

        Mockito.when(allocationService.allocateStock(ArgumentMatchers.any(AllocationRequest.class))).thenReturn(response);

        // Bypassing static methods entirely using direct class references
        mockMvc.perform(MockMvcRequestBuilders.post("/api/allocations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("SUCCESS"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.quantity").value(5));
    }
}