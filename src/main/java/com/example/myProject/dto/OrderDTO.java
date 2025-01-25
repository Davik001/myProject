package com.example.myProject.dto;

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

    private long id;
    private LocalDateTime orderDate;
    private String orderStatus;
    private long customerId;

    private CustomerDTO customer; // инфа про клиента. Можем использовать геттеры и сеттеры при надобности
}
