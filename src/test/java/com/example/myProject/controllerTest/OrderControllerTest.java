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
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    ObjectMapper objectMapper = new ObjectMapper();

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

}
