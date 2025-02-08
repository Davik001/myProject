package com.example.myProject.entity;

import com.example.myProject.orderStatus.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    OrderStatus orderStatus;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    Customer customer;


    @ManyToMany
    @JoinTable(
            name = "order_products",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    List<Product> products = new ArrayList<>();

    public Order(long id, LocalDateTime orderDate, OrderStatus orderStatus, Customer customer) {
        this.id = id;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.customer = customer;
    }

    public Order(long id, LocalDateTime orderDate, OrderStatus orderStatus) {
        this.id = id;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
    }
}
