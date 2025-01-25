package com.example.myProject.specifications;

import com.example.myProject.entity.Customer;
import com.example.myProject.entity.Order;
import com.example.myProject.orderStatus.OrderStatus;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class OrderSpecifications {

    // Фильтрация по дате заказа
    public static Specification<Order> filterByOrderDate(LocalDateTime orderDate) {
        return (root, query, criteriaBuilder) -> {
            if (orderDate != null) {
                return criteriaBuilder.equal(root.get("orderDate"), orderDate);
            }
            return null;
        };
    }

    // Фильтрация по статусу заказа
    public static Specification<Order> filterByOrderStatus(OrderStatus orderStatus) {
        return (root, query, criteriaBuilder) -> {
            if (orderStatus != null) {
                return criteriaBuilder.equal(root.get("orderStatus"), orderStatus);
            }
            return null;
        };
    }

    // Фильтрация по клиенту
    public static Specification<Order> filterByCustomer(Long customerId) {
        return (root, query, criteriaBuilder) -> {
            if (customerId != null) {
                Join<Order, Customer> customer = root.join("customer");
                return criteriaBuilder.equal(customer.get("id"), customerId);
            }
            return null;
        };
    }

    // Метод для создания спецификации с фильтрами
    public static Specification<Order> getSpecification(LocalDateTime orderDate, OrderStatus orderStatus, Long customerId) {
        Specification<Order> specification = Specification.where(null);

        if (orderDate != null) {
            specification = specification.and(filterByOrderDate(orderDate));
        }
        if (orderStatus != null) {
            specification = specification.and(filterByOrderStatus(orderStatus));
        }
        if (customerId != null) {
            specification = specification.and(filterByCustomer(customerId));
        }

        return specification;
    }
}
