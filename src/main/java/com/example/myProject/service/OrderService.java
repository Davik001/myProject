package com.example.myProject.service;

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
import com.example.myProject.specifications.OrderSpecifications;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
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
                .orElseThrow(() -> new RuntimeException("Customer not found"));

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
                .orElseThrow(() -> new RuntimeException("Order not found"));
        repository.delete(order);
    }

    // Обновление заказа
    public OrderResponseDTO updateOrder(long id, OrderUpdateDTO dto) {
        Order order = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (dto.getOrderStatus() != null) {
            try {
                order.setOrderStatus(OrderStatus.valueOf(dto.getOrderStatus().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid order status");
            }
        }

        if (dto.getCustomerId() != null) {
            Customer customer = customerRepository.findById(dto.getCustomerId())
                    .orElseThrow(() -> new RuntimeException("Customer not found"));
            order.setCustomer(customer);
        }

        order = repository.save(order);
        return mapper.toDTO(order);
    }

    // Получение заказа по ID
    public OrderResponseDTO getOrder(long id) {
        Order order = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
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


