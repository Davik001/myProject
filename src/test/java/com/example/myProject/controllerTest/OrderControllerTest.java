package com.example.myProject.controllerTest;

import com.example.myProject.controllers.OrderController;
import com.example.myProject.dto.alldtos.OrderDTO;
import com.example.myProject.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void addModule(){
        objectMapper.registerModule(new JavaTimeModule()); // Jackson не поддерживает преобразование типов из 8 джавы, поэтому надо добавить модуль
    }

    @Test
    void testCreateOrder() throws Exception {
        OrderDTO request = new OrderDTO(null, LocalDateTime.now(), "NEW", 1L);
        OrderDTO response = new OrderDTO(1L, request.getOrderDate(), request.getOrderStatus(), request.getCustomerId());

        Mockito.when(orderService.createOrder(Mockito.any(OrderDTO.class))).thenReturn(response);

        mockMvc.perform(post("/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.orderStatus").value("NEW"))
                .andExpect(jsonPath("$.customerId").value(1));
    }

    @Test
    void testGetOrder() throws Exception {
        OrderDTO orderDTO = new OrderDTO(1L, LocalDateTime.now(), "NEW", 1L);

        Mockito.when(orderService.getOrder(1L)).thenReturn(orderDTO);

        mockMvc.perform(get("/order/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.orderStatus").value("NEW"));
    }

    @Test
    void testDeleteOrder() throws Exception {
        Mockito.doNothing().when(orderService).deleteOrder(1L);

        mockMvc.perform(delete("/order/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAllOrders() throws Exception {

    }


}
