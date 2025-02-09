package com.example.myProject.serviceTest;

import com.example.myProject.dto.alldtos.CustomerDTO;
import com.example.myProject.entity.Customer;
import com.example.myProject.map.CustomerMapper;
import com.example.myProject.repository.CustomerRepository;
import com.example.myProject.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    CustomerRepository customerRepository;

    @Mock
    CustomerMapper customerMapper;

    @InjectMocks // внедряем в сервис фейковые объекты маппера и репозитория
    CustomerService customerService;

    Customer customer;
    CustomerDTO customerDTO;



}
