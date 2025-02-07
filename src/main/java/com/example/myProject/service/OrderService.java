package com.example.myProject.service;

import com.example.myProject.dto.alldtos.OrderDTO;
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
    OrderRepository repository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    OrderMapper mapper;

    // создаем
    public OrderDTO createOrder(OrderDTO dto) {
        // Сначала сохраняем клиента
        Customer customer = customerRepository.findById(dto.getCustomer().getId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // Создаем заказ и привязываем к клиенту
        Order order = mapper.toEntity(dto);
        order.setCustomer(customer);

        // Сохраняем заказ
        order = repository.save(order);
        return mapper.toDTO(order);
    }

    // удаляем
    public void deleteOrder(long id) {
        Order order = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        repository.delete(order);
    }

    // обновляем
    public OrderDTO updateOrder(long id, OrderDTO dto) {
        Order order = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));


        try {
            // Преобразуем строку в enum
            OrderStatus status = OrderStatus.valueOf(dto.getOrderStatus());
            order.setOrderStatus(status);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Error in order status");
        }

        order = repository.save(order);
        return mapper.toDTO(order);
    }

    // чтение
    public OrderDTO getOrder(long id) {
        Order order = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return mapper.toDTO(order);
    }

    public Page<OrderDTO> getOrders(OrderStatus status, LocalDateTime orderDate, long customerId, int size, int page) {
        Specification<Order> specification = OrderSpecifications.getSpecification(orderDate, status, customerId);

        PageRequest pageable = PageRequest.of(page, size);
        Page<Order> ordersPage = repository.findAll(specification, pageable);

        // Преобразование результатов в DTO
        return ordersPage.map(mapper::toDTO);
    }


}

