package com.example.myProject.dto.alldtos;

import com.example.myProject.orderStatus.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {

    private Long id;
    private LocalDateTime orderDate;
    private String orderStatus;
    private Long customerId;

    private CustomerDTO customer; // инфа про клиента. Можем использовать геттеры и сеттеры при надобности

    public OrderDTO(Long id, LocalDateTime orderDate, String orderStatus, Long customerId) {
        this.id = id;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.customerId = customerId;
    }

    public OrderDTO(Long id, LocalDateTime orderDate, String orderStatus) {
        this.id = id;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
    }
}
