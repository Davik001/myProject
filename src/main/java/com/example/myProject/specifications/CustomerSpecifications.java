package com.example.myProject.specifications;

import com.example.myProject.entity.Customer;
import com.example.myProject.entity.Order;
import com.example.myProject.projection.DataCustomer;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class CustomerSpecifications {
    // Фильтрация по имени
    public static Specification<Customer> filterByFirstName(String firstName) {
        return (root, query, criteriaBuilder) -> {
            if (firstName != null && !firstName.isEmpty()) {
                return criteriaBuilder.like(root.get("firstName"), "%" + firstName + "%");
            }
            return null;
        };
    }

    // Фильтрация по фамилии
    public static Specification<Customer> filterByLastName(String lastName) {
        return (root, query, criteriaBuilder) -> {
            if (lastName != null && !lastName.isEmpty()) {
                return criteriaBuilder.like(root.get("lastName"), "%" + lastName + "%");
            }
            return null;
        };
    }

    // Фильтрация по email
    public static Specification<Customer> filterByEmail(String email) {
        return (root, query, criteriaBuilder) -> {
            if (email != null && !email.isEmpty()) {
                return criteriaBuilder.like(root.get("email"), "%" + email + "%");
            }
            return null;
        };
    }

    // Фильтрация по телефону
    public static Specification<Customer> filterByPhone(String phone) {
        return (root, query, criteriaBuilder) -> {
            if (phone != null && !phone.isEmpty()) {
                return criteriaBuilder.like(root.get("phone"), "%" + phone + "%");
            }
            return null;
        };
    }

    // Фильтрация по заказам
    public static Specification<Customer> filterByOrders(String status) {
        return (root, query, criteriaBuilder) -> {
            if (status != null && !status.isEmpty()) {
                // Создаем join с заказами
                Join<Customer, Order> orders = root.join("orders");
                return criteriaBuilder.equal(orders.get("status"), status); // Фильтрация по статусу заказа
            }
            return null;
        };
    }

    public static Specification<Customer> getSpecification(DataCustomer dataCustomer) {
        Specification<Customer> specification = Specification.where(null);

        if (dataCustomer.getFirstName() != null) {
            specification = specification.and(filterByFirstName(dataCustomer.getFirstName()));
        }
        if (dataCustomer.getLastName() != null) {
            specification = specification.and(filterByLastName(dataCustomer.getLastName()));
        }
        if (dataCustomer.getEmail() != null) {
            specification = specification.and(filterByEmail(dataCustomer.getEmail()));
        }
        if (dataCustomer.getPhone() != null) {
            specification = specification.and(filterByPhone(dataCustomer.getPhone()));
        }
       if (dataCustomer.getOrderStatus() != null) {
         specification = specification.and(filterByOrders(dataCustomer.getOrderStatus()));
       }

        return specification;
    }

}
