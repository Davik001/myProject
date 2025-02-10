package com.example.myProject.serviceTest;

import com.example.myProject.dto.alldtos.CustomerDTO;
import com.example.myProject.dto.alldtos.OrderDTO;
import com.example.myProject.dto.common.OrderResponseDTO;
import com.example.myProject.dto.create.OrderCreateDTO;
import com.example.myProject.dto.update.OrderUpdateDTO;
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
    private OrderRepository orderRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderService orderService;

    private Order order;
    private OrderResponseDTO orderResponseDTO;
    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer(1L, "John", "Doe", "john.doe@example.com", "1234567890", Collections.emptyList());
        order = new Order(1L, LocalDateTime.now(), OrderStatus.NEW, customer);
        orderResponseDTO = new OrderResponseDTO(1L, order.getOrderDate(), "NEW", customer.getId(), null);
    }

    @Test
    void testCreateOrder() {
        OrderCreateDTO orderCreateDTO = new OrderCreateDTO("NEW", customer.getId());
        when(customerRepository.findById(orderCreateDTO.getCustomerId())).thenReturn(Optional.of(customer));
        when(orderMapper.toEntity(orderCreateDTO)).thenReturn(order);
        when(orderRepository.save(order)).thenReturn(order);
        when(orderMapper.toDTO(order)).thenReturn(orderResponseDTO);

        OrderResponseDTO result = orderService.createOrder(orderCreateDTO);

        assertNotNull(result);
        assertEquals(orderResponseDTO.getId(), result.getId());
        assertEquals(orderResponseDTO.getOrderStatus(), result.getOrderStatus());
        assertEquals(orderResponseDTO.getCustomerId(), result.getCustomerId());

        verify(customerRepository, times(1)).findById(orderCreateDTO.getCustomerId());
        verify(orderRepository, times(1)).save(order);
        verify(orderMapper, times(1)).toDTO(order);
    }

    @Test
    void testGetOrder_Success() {
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        when(orderMapper.toDTO(order)).thenReturn(orderResponseDTO);

        OrderResponseDTO result = orderService.getOrder(order.getId());

        assertNotNull(result);
        assertEquals(orderResponseDTO.getId(), result.getId());
        assertEquals(orderResponseDTO.getOrderStatus(), result.getOrderStatus());

        verify(orderRepository, times(1)).findById(order.getId());
        verify(orderMapper, times(1)).toDTO(order);
    }

    @Test
    void testGetOrder_NotFound() {
        when(orderRepository.findById(order.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> orderService.getOrder(order.getId()));
        assertEquals("Order not found", exception.getMessage());

        verify(orderRepository, times(1)).findById(order.getId());
    }

    @Test
    void testUpdateOrder_Success() {
        OrderUpdateDTO orderUpdateDTO = new OrderUpdateDTO(order.getId(), "PROCESSING", customer.getId());

        when(customerRepository.findById(orderUpdateDTO.getCustomerId())).thenReturn(Optional.of(customer));
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));

        doAnswer(invocation -> {
            Order savedOrder = invocation.getArgument(0);
            savedOrder.setOrderStatus(OrderStatus.PROCESSING);
            return savedOrder;
        }).when(orderRepository).save(any(Order.class));

        when(orderMapper.toDTO(order)).thenReturn(new OrderResponseDTO(
                order.getId(), order.getOrderDate(), "PROCESSING", order.getCustomer().getId(), null));

        OrderResponseDTO result = orderService.updateOrder(order.getId(), orderUpdateDTO);

        assertNotNull(result);
        assertEquals("PROCESSING", result.getOrderStatus());

        verify(customerRepository, times(1)).findById(orderUpdateDTO.getCustomerId());
        verify(orderRepository, times(1)).findById(order.getId());
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(orderMapper, times(1)).toDTO(order);
    }



    @Test
    void testUpdateOrder_NotFound() {
        OrderUpdateDTO orderUpdateDTO = new OrderUpdateDTO(order.getId(), "PROCESSING", customer.getId());
        when(orderRepository.findById(order.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> orderService.updateOrder(order.getId(), orderUpdateDTO));
        assertEquals("Order not found", exception.getMessage());

        verify(orderRepository, times(1)).findById(order.getId());
    }

    @Test
    void testDeleteOrder_Success() {
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));

        orderService.deleteOrder(order.getId());

        verify(orderRepository, times(1)).findById(order.getId());
        verify(orderRepository, times(1)).delete(order);
    }

    @Test
    void testDeleteOrder_NotFound() {
        when(orderRepository.findById(order.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> orderService.deleteOrder(order.getId()));
        assertEquals("Order not found", exception.getMessage());

        verify(orderRepository, times(1)).findById(order.getId());
    }
}