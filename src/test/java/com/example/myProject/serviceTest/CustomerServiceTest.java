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

    @BeforeEach
    void setUp(){
        customer = new Customer(1L, "John", "Doe", "john.doe@example.com",
                "1234567890", Collections.emptyList());

        customerDTO = new CustomerDTO(1L, "John", "Doe", "john.doe@example.com",
                "1234567890", Collections.emptyList());
    }

    // тесты для чтения
    @Test
    void testGetCustomer_Success(){
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerMapper.toDto(customer)).thenReturn(customerDTO);

        CustomerDTO result = customerService.getCustomer(1L);

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());

        verify(customerRepository, times(1)).findById(1L);
        verify(customerMapper, times(1)).toDto(customer);
    }

    @Test
    void testGetCustomer_NotFound(){
        when(customerRepository.findById(2L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> customerService.getCustomer(2L));

        assertEquals("Customer not found", exception.getMessage());

        verify(customerRepository, times(1)).findById(2L);
       // verify(customerRepository, never()).save(any());
    }

    // cоздание
    @Test
    void testCreateCustomer(){
        when(customerMapper.toEntity(customerDTO)).thenReturn(customer);
        when(customerRepository.save(customer)).thenReturn(customer);
        when(customerMapper.toDto(customer)).thenReturn(customerDTO);

        CustomerDTO result = customerService.createCustomer(customerDTO);

        assertNotNull(result);
        assertEquals("John", result.getFirstName());

        verify(customerMapper, times(1)).toEntity(customerDTO);
        verify(customerRepository, times(1)).save(customer);
        verify(customerMapper, times(1)).toDto(customer);
    }

    // обновление значении
    @Test
    void testUpdateCustomer_Success(){
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(customerMapper.toDto(any(Customer.class))).thenAnswer(invocation -> {
            Customer updatedCustomer = invocation.getArgument(0);
            return new CustomerDTO(
                    updatedCustomer.getId(),
                    updatedCustomer.getFirstName(),
                    updatedCustomer.getLastName(),
                    updatedCustomer.getEmail(),
                    updatedCustomer.getPhone()
            );
        });

        CustomerDTO update = new CustomerDTO(1L, "Jane", "Doe", "jane.doe@example.com", "0987654321");

        // Обновление данных в customer
        customer.setFirstName(update.getFirstName());
        customer.setLastName(update.getLastName());
        customer.setEmail(update.getEmail());
        customer.setPhone(update.getPhone());

        CustomerDTO result = customerService.updateCustomer(1L, update);

        assertNotNull(result);
        assertEquals("Jane", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("jane.doe@example.com", result.getEmail());
        assertEquals("0987654321", result.getPhone());

        verify(customerRepository, times(1)).findById(1L);
        verify(customerRepository, times(1)).save(any(Customer.class));
        verify(customerMapper, times(1)).toDto(any(Customer.class));
    }

    @Test
    void testUpdateCustomer_NotFound(){
        when(customerRepository.findById(2L)).thenReturn(Optional.empty());

        CustomerDTO updatedDTO = new CustomerDTO(2L, "Jane", "Doe", "jane.doe@example.com", "0987654321");

        Exception exception = assertThrows(RuntimeException.class, () -> customerService.updateCustomer(2L, updatedDTO));

        assertEquals("Customer not found", exception.getMessage());

        verify(customerRepository, times(1)).findById(2L);
        verify(customerRepository, never()).save(any());
    }

    // удаление
    @Test
    void testDeleteCustomer_Success(){
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        // Мокаем delete, чтобы он ничего не делал (для void методов)
        doNothing().when(customerRepository).delete(any(Customer.class));

        customerService.deleteCustomer(1L);

        verify(customerRepository, times(1)).findById(1L);
        verify(customerRepository, times(1)).delete(any(Customer.class));
    }

    @Test
    void testDeleteCustomer_NotFound(){
        when(customerRepository.findById(2L)).thenReturn(Optional.empty());
        Exception exception = assertThrows(RuntimeException.class, () -> customerService.deleteCustomer(2L));

        assertEquals("Customer not found", exception.getMessage());

        verify(customerRepository, times(1)).findById(2L);
        verify(customerRepository, never()).deleteById(2L);
    }

}
