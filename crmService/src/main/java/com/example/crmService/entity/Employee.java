package com.example.crmService.entity;

import com.example.crmService.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column
    String firstName;
    @Column
    String lastName;

    @Column(unique=true)
    String email;

    @Column
    String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    Role role;

}
