package com.example.myProject.serviceTest;

import com.example.myProject.dto.alldtos.OrderDTO;
import com.example.myProject.entity.Order;
import com.example.myProject.map.OrderMapper;
import com.example.myProject.repository.OrderRepository;
import com.example.myProject.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    OrderRepository orderRepository;

    @Mock
    OrderMapper orderMapper;

    @InjectMocks
    OrderService orderService;

    Order order;
    OrderDTO orderDTO;

    @BeforeEach
    void setUp(){
        order = new Order();
    }
}
