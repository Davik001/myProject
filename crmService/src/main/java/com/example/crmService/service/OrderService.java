package com.example.crmService.service;


import com.example.crmService.dto.common.OrderResponseDTO;
import com.example.crmService.dto.create.OrderCreateDTO;
import com.example.crmService.dto.update.OrderUpdateDTO;
import com.example.crmService.entity.Customer;
import com.example.crmService.entity.Order;
import com.example.crmService.map.OrderMapper;
import com.example.crmService.orderStatus.OrderStatus;
import com.example.crmService.repository.CustomerRepository;
import com.example.crmService.repository.OrderRepository;
import com.example.crmService.specifications.OrderSpecifications;
import jakarta.persistence.EntityNotFoundException;
import jakarta.xml.bind.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OrderService {

    @Autowired
    private OrderRepository repository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderMapper mapper;

    // Создание заказа
    public OrderResponseDTO createOrder(OrderCreateDTO dto) {
        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));

        Order order = mapper.toEntity(dto);
        order.setOrderStatus(OrderStatus.valueOf(dto.getOrderStatus().toUpperCase()));
        order.setOrderDate(LocalDateTime.now());
        order.setCustomer(customer);

        order = repository.save(order);
        return mapper.toDTO(order);
    }

    // Удаление заказа
    public void deleteOrder(long id) {
        Order order = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
        repository.delete(order);
    }

    // Обновление заказа
    public OrderResponseDTO updateOrder(long id, OrderUpdateDTO dto) {
        Order order = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        if (dto.getOrderStatus() != null) {
            try {
                order.setOrderStatus(OrderStatus.valueOf(dto.getOrderStatus().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid order status");
            }
        }

        if (dto.getCustomerId() != null) {
            Customer customer = customerRepository.findById(dto.getCustomerId())
                    .orElseThrow(() -> new EntityNotFoundException("Customer not found"));
            order.setCustomer(customer);
        }

        order = repository.save(order);
        return mapper.toDTO(order);
    }

    // Получение заказа по ID
    public OrderResponseDTO getOrder(long id) {
        Order order = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
        return mapper.toDTO(order);
    }

    // Получение списка заказов с фильтрацией, сортировкой и пагинацией
    public Page<OrderResponseDTO> getOrders(OrderStatus status, LocalDateTime orderDate, Long customerId, int page, int size) {
        Specification<Order> specification = OrderSpecifications.getSpecification(orderDate, status, customerId);
        PageRequest pageable = PageRequest.of(page, size);
        Page<Order> ordersPage = repository.findAll(specification, pageable);

        return ordersPage.map(mapper::toDTO);
    }
}


