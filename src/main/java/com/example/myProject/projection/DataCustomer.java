package com.example.myProject.projection;

import com.example.myProject.orderStatus.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DataCustomer {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String orderStatus;
}
