package com.example.myProject.dto.alldtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO {

    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    private List<OrderDTO> orders; // инфа про заказы. Используя преобразования, геттеры/сеттеры и конструкторы

    public CustomerDTO(long id, String firstName, String lastName, String email, String phone) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
    }
}
