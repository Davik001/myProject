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

    Order order;
    OrderDTO orderDTO;
    Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer(1L, "John", "Doe", "john.doe@example.com",
                "1234567890", Collections.emptyList());
        order = new Order(1L, LocalDateTime.now(), OrderStatus.NEW, customer);
        orderDTO = new OrderDTO(1L, LocalDateTime.now(), "NEW", customer.getId());
    }

    // Тест для создания заказа
    @Test
    void testCreateOrder() {
        CustomerDTO customerDTO = new CustomerDTO(1L, "John", "Doe", "john.doe@example.com",
                "1234567890", Collections.emptyList());

        orderDTO = new OrderDTO(1L, LocalDateTime.now(), "NEW", 1L);
        orderDTO.setCustomer(customerDTO);

        when(customerRepository.findById(customerDTO.getId())).thenReturn(Optional.of(customer));
        when(orderMapper.toEntity(orderDTO)).thenReturn(order);
        when(orderRepository.save(order)).thenReturn(order);
        when(orderMapper.toDTO(order)).thenReturn(orderDTO);

        OrderDTO result = orderService.createOrder(orderDTO);

        assertNotNull(result);
        assertEquals(orderDTO.getId(), result.getId());
        assertEquals(orderDTO.getOrderStatus(), result.getOrderStatus());
        assertEquals(orderDTO.getCustomerId(), result.getCustomerId());

        verify(customerRepository, times(1)).findById(customerDTO.getId());
        verify(orderRepository, times(1)).save(order);
        verify(orderMapper, times(1)).toDTO(order);
    }


    // для получения заказа
    @Test
    void testGetOrder_Success() {
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        when(orderMapper.toDTO(order)).thenReturn(orderDTO);

        OrderDTO result = orderService.getOrder(order.getId());

        assertNotNull(result);
        assertEquals(orderDTO.getId(), result.getId());
        assertEquals(orderDTO.getOrderStatus(), result.getOrderStatus());

        verify(orderRepository, times(1)).findById(order.getId());
        verify(orderMapper, times(1)).toDTO(order);
    }

    // Тест для получения заказа
    @Test
    void testGetOrder_NotFound() {
        when(orderRepository.findById(order.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> orderService.getOrder(order.getId()));
        assertEquals("Order not found", exception.getMessage());

        verify(orderRepository, times(1)).findById(order.getId());
    }

    // для обновления заказа
    @Test
    void testUpdateOrder_Success() {
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);
        when(orderMapper.toDTO(order)).thenReturn(orderDTO);

        orderDTO.setOrderStatus("PROCESSING");

        OrderDTO result = orderService.updateOrder(order.getId(), orderDTO);

        assertNotNull(result);
        assertEquals("PROCESSING", result.getOrderStatus());

        verify(orderRepository, times(1)).findById(order.getId());
        verify(orderRepository, times(1)).save(order);
        verify(orderMapper, times(1)).toDTO(order);
    }

    // Тест для обновления заказа, когда заказ не найден
    @Test
    void testUpdateOrder_NotFound() {
        when(orderRepository.findById(order.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> orderService.updateOrder(order.getId(), orderDTO));
        assertEquals("Order not found", exception.getMessage());

        verify(orderRepository, times(1)).findById(order.getId());
    }

    // Тест для удаления заказа
    @Test
    void testDeleteOrder_Success() {
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));

        orderService.deleteOrder(order.getId());

        verify(orderRepository, times(1)).findById(order.getId());
        verify(orderRepository, times(1)).delete(order);
    }

    // Тест для удаления заказа, когда заказ не найден
    @Test
    void testDeleteOrder_NotFound() {
        when(orderRepository.findById(order.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> orderService.deleteOrder(order.getId()));
        assertEquals("Order not found", exception.getMessage());

        verify(orderRepository, times(1)).findById(order.getId());
    }


}
