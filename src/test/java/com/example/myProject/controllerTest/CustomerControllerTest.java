package com.example.myProject.controllerTest;


import com.example.myProject.dto.alldtos.CustomerDTO;
import com.example.myProject.dto.common.CustomerResponseDTO;
import com.example.myProject.dto.create.CustomerCreateDTO;
import com.example.myProject.dto.update.CustomerUpdateDTO;
import com.example.myProject.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CustomerService customerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateCustomer() throws Exception {
        CustomerCreateDTO request = new CustomerCreateDTO("John", "Doe", "john@example.com", "123456789");
        CustomerResponseDTO response = new CustomerResponseDTO(1L, "John", "Doe", "john@example.com", "123456789");

        Mockito.when(customerService.createCustomer(Mockito.any(CustomerCreateDTO.class))).thenReturn(response);

        mockMvc.perform(post("/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    void testUpdateCustomer() throws Exception {
        Long customerId = 1L;
        CustomerUpdateDTO requestDTO = new CustomerUpdateDTO("John", "Smith", "john.smith@example.com", "987654321");
        CustomerResponseDTO responseDTO = new CustomerResponseDTO(customerId, "John", "Smith", "john.smith@example.com", "987654321");

        Mockito.when(customerService.updateCustomer(Mockito.eq(customerId), Mockito.any(CustomerUpdateDTO.class)))
                .thenReturn(responseDTO);

        mockMvc.perform(put("/customer/{id}", customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(customerId))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Smith"))
                .andExpect(jsonPath("$.email").value("john.smith@example.com"));
    }

    @Test
    void testDeleteCustomer() throws Exception {
        Long customerId = 1L;

        Mockito.doNothing().when(customerService).deleteCustomer(customerId);

        mockMvc.perform(delete("/customer/{id}", customerId))
                .andExpect(status().isOk());
    }

    @Test
    void testGetCustomer() throws Exception {
        Long customerId = 1L;
        CustomerResponseDTO responseDTO = new CustomerResponseDTO(customerId, "John", "Doe", "john@example.com", "123456789");

        Mockito.when(customerService.getCustomer(customerId)).thenReturn(responseDTO);

        mockMvc.perform(get("/customer/{id}", customerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(customerId))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    void testGetAllCustomers() throws Exception {
        Page<CustomerResponseDTO> mockPage = new PageImpl<>(List.of(
                new CustomerResponseDTO(1L, "John", "Doe", "john@example.com", "123456789"),
                new CustomerResponseDTO(2L, "Jane", "Smith", "jane@example.com", "987654321")
        ));

        Mockito.when(customerService.getAllCustomers(Mockito.any(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(mockPage);

        mockMvc.perform(get("/customer")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].firstName").value("John"))
                .andExpect(jsonPath("$.content[1].firstName").value("Jane"));
    }
}
