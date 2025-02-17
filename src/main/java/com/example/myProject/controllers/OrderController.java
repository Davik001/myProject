package com.example.myProject.controllers;

import com.example.myProject.dto.alldtos.OrderDTO;
import com.example.myProject.dto.common.OrderResponseDTO;
import com.example.myProject.dto.create.OrderCreateDTO;
import com.example.myProject.dto.update.OrderUpdateDTO;
import com.example.myProject.orderStatus.OrderStatus;
import com.example.myProject.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/order")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @PostMapping
    public OrderResponseDTO createOrder(@RequestBody OrderCreateDTO orderDTO) {
        logger.info("Creating Order {}", orderDTO);
        return orderService.createOrder(orderDTO);
    }

    @PutMapping("/{id}")
    public OrderResponseDTO updateOrder(@PathVariable Long id, @RequestBody OrderUpdateDTO orderDTO) {
        logger.info("Updating Order with id: {}, new data{}", id, orderDTO);

        try {
            return orderService.updateOrder(id, orderDTO);
        } catch (Exception e) {
            logger.error("Error updating Order with id: {}, {}", id, orderDTO);
            throw e;
        }
    }

    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable Long id) {
        logger.info("Deleting Order with id: {}", id);

        try {
            orderService.deleteOrder(id);
        } catch (Exception e) {
            logger.error("Error deleting Order with id: {}, {}", id, e.getMessage());
            throw e;
        }
    }

    @GetMapping("/{id}")
    public OrderResponseDTO getOrder(@PathVariable Long id) {
        logger.info("Getting Order with id: {}", id);
        try {
            return orderService.getOrder(id);
        } catch (Exception e) {
            logger.error("Error retrieving Order with id: {}, {}", id, e.getMessage());
            throw e;
        }
    }

    @GetMapping
    public Page<OrderResponseDTO> getOrders(
            @RequestParam(required = false) String orderStatus,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime orderDate,
            @RequestParam(required = false) Long customerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        logger.info("Getting Orders with filter: status: {}, date: {}, id: {}, page: {}, size: {} ",
                orderStatus, orderDate, customerId, page, size);

        OrderStatus status = (orderStatus != null) ? OrderStatus.valueOf(orderStatus.toUpperCase()) : null;
        return orderService.getOrders(status, orderDate, customerId, page, size);
    }
}

