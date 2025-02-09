package com.example.myProject.serviceTest;

import com.example.myProject.dto.alldtos.CustomerDTO;
import com.example.myProject.dto.alldtos.OrderDTO;
import com.example.myProject.entity.Customer;
import com.example.myProject.entity.Order;
import com.example.myProject.map.OrderMapper;
import com.example.myProject.orderStatus.OrderStatus;
import com.example.myProject.repository.CustomerRepository;
import com.example.myProject.repository.OrderRepository;
import com.example.myProject.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    OrderRepository orderRepository;

    @Mock
    CustomerRepository customerRepository;

    @Mock
    OrderMapper orderMapper;

    @InjectMocks
    OrderService orderService;



}
