package com.example.myProject.controllerTest;

import com.example.myProject.controllers.OrderController;
import com.example.myProject.dto.alldtos.OrderDTO;
import com.example.myProject.dto.common.OrderResponseDTO;
import com.example.myProject.dto.create.OrderCreateDTO;
import com.example.myProject.dto.update.OrderUpdateDTO;
import com.example.myProject.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
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

import java.time.LocalDateTime;
import java.util.Arrays;
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
    private ObjectMapper objectMapper;

    @BeforeEach
    void addModule() {
        objectMapper.registerModule(new JavaTimeModule()); // Jackson не поддерживает преобразование типов из 8 джавы, поэтому надо добавить модуль
    }

    @Test
    void testCreateOrder() throws Exception {
        OrderCreateDTO request = new OrderCreateDTO("NEW", 1L);
        OrderResponseDTO response = new OrderResponseDTO(1L, LocalDateTime.now(), "NEW", 1L, null);

        Mockito.when(orderService.createOrder(Mockito.any(OrderCreateDTO.class))).thenReturn(response);

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
        OrderResponseDTO orderDTO = new OrderResponseDTO(1L, LocalDateTime.now(), "NEW", 1L, null);

        Mockito.when(orderService.getOrder(1L)).thenReturn(orderDTO);

        mockMvc.perform(get("/order/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.orderStatus").value("NEW"))
                .andExpect(jsonPath("$.customerId").value(1L));
    }

    @Test
    void testDeleteOrder() throws Exception {
        Mockito.doNothing().when(orderService).deleteOrder(1L);

        mockMvc.perform(delete("/order/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAllOrders() throws Exception {
        OrderResponseDTO order1 = new OrderResponseDTO(1L, LocalDateTime.now(), "NEW", 1L, null);
        OrderResponseDTO order2 = new OrderResponseDTO(2L, LocalDateTime.now(), "PROCESSING", 2L, null);

        List<OrderResponseDTO> orders = Arrays.asList(order1, order2);
        Page<OrderResponseDTO> orderPage = new PageImpl<>(orders);

        Mockito.when(orderService.getOrders(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(orderPage);

        mockMvc.perform(get("/order")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[1].id").value(2L))
                .andExpect(jsonPath("$.content[0].orderStatus").value("NEW"))
                .andExpect(jsonPath("$.content[1].orderStatus").value("PROCESSING"));
    }
}
