package com.example.myProject.controllerTest;


import com.example.myProject.controllers.CustomerController;
import com.example.myProject.dto.alldtos.CustomerDTO;
import com.example.myProject.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;


import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    CustomerService customerService;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testCreateCustomer() throws Exception {
        CustomerDTO request = new CustomerDTO(null, "John", "Doe", "john@example.com", "123456789");
        CustomerDTO response = new CustomerDTO(1L, "John", "Doe", "john@example.com", "123456789");

        Mockito.when(customerService.createCustomer(Mockito.any(CustomerDTO.class))).thenReturn(response);


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
        CustomerDTO requestDTO = new CustomerDTO(null, "John", "Smith", "john.smith@example.com", "987654321");
        CustomerDTO responseDTO = new CustomerDTO(customerId, "John", "Smith", "john.smith@example.com", "987654321");

        Mockito.when(customerService.updateCustomer(Mockito.eq(customerId), Mockito.any(CustomerDTO.class)))
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
        CustomerDTO responseDTO = new CustomerDTO(customerId, "John", "Doe", "john@example.com", "123456789");

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
        /* PageImpl — это готовая реализация интерфейса Page<T>.
        Она позволяет эмулировать поведение постраничного вывода (пагинации) без реального запроса в базу данных. */
        Page<CustomerDTO> mockPage = new PageImpl<>(List.of(
                new CustomerDTO(1L, "John", "Doe", "john@example.com", "123456789"),
                new CustomerDTO(2L, "Jane", "Smith", "jane@example.com", "987654321")
        ));
        // PageImpl<T> используется, когда нужно подделать пагинацию в тестах

        Mockito.when(customerService.getAllCustomer(Mockito.any(), Mockito.anyInt(), Mockito.anyInt()))
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
