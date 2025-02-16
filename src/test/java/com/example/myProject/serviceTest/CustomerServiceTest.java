package com.example.myProject.serviceTest;

import com.example.myProject.dto.common.CustomerResponseDTO;
import com.example.myProject.dto.create.CustomerCreateDTO;
import com.example.myProject.dto.update.CustomerUpdateDTO;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerService customerService;

    private Customer customer;
    private CustomerResponseDTO customerResponseDTO;
    private CustomerCreateDTO customerCreateDTO;
    private CustomerUpdateDTO customerUpdateDTO;

    @BeforeEach
    void setUp() {
        customer = new Customer(1L, "John", "Doe", "john.doe@example.com", "1234567890");
        customerResponseDTO = new CustomerResponseDTO(1L, "John", "Doe", "john.doe@example.com", "1234567890");
        customerCreateDTO = new CustomerCreateDTO("John", "Doe", "john.doe@example.com", "1234567890");
        customerUpdateDTO = new CustomerUpdateDTO(1L, "Jane", "Doe", "jane.doe@example.com", "0987654321");
    }

    @Test
    void testGetCustomer_Success() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerMapper.toDto(customer)).thenReturn(customerResponseDTO);

        CustomerResponseDTO result = customerService.getCustomer(1L);

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());

        verify(customerRepository, times(1)).findById(1L);
        verify(customerMapper, times(1)).toDto(customer);
    }

    @Test
    void testGetCustomer_NotFound() {
        when(customerRepository.findById(2L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> customerService.getCustomer(2L));

        assertEquals("Customer not found", exception.getMessage());

        verify(customerRepository, times(1)).findById(2L);
    }

    @Test
    void testCreateCustomer() {
        when(customerMapper.toEntity(customerCreateDTO)).thenReturn(customer);
        when(customerRepository.save(customer)).thenReturn(customer);
        when(customerMapper.toDto(customer)).thenReturn(customerResponseDTO);

        CustomerResponseDTO result = customerService.createCustomer(customerCreateDTO);

        assertNotNull(result);
        assertEquals("John", result.getFirstName());

        verify(customerMapper, times(1)).toEntity(customerCreateDTO);
        verify(customerRepository, times(1)).save(customer);
        verify(customerMapper, times(1)).toDto(customer);
    }

    @Test
    void testUpdateCustomer_Success() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerMapper.toEntity(customerUpdateDTO, customer)).thenReturn(customer);
        when(customerRepository.save(customer)).thenReturn(customer);
        when(customerMapper.toDto(customer)).thenReturn(new CustomerResponseDTO(
                1L, "Jane", "Doe", "jane.doe@example.com", "0987654321"));

        CustomerResponseDTO result = customerService.updateCustomer(1L, customerUpdateDTO);

        assertNotNull(result);
        assertEquals("Jane", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("jane.doe@example.com", result.getEmail());
        assertEquals("0987654321", result.getPhone());

        verify(customerRepository, times(1)).findById(1L);
        verify(customerMapper, times(1)).toEntity(customerUpdateDTO, customer);
        verify(customerRepository, times(1)).save(customer);
        verify(customerMapper, times(1)).toDto(customer);
    }

    @Test
    void testUpdateCustomer_NotFound() {
        when(customerRepository.findById(2L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> customerService.updateCustomer(2L, customerUpdateDTO));

        assertEquals("Customer not found", exception.getMessage());

        verify(customerRepository, times(1)).findById(2L);
        verify(customerRepository, never()).save(any());
    }

    @Test
    void testDeleteCustomer_Success() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        doNothing().when(customerRepository).delete(any(Customer.class));

        customerService.deleteCustomer(1L);

        verify(customerRepository, times(1)).findById(1L);
        verify(customerRepository, times(1)).delete(any(Customer.class));
    }

    @Test
    void testDeleteCustomer_NotFound() {
        when(customerRepository.findById(2L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> customerService.deleteCustomer(2L));

        assertEquals("Customer not found", exception.getMessage());

        verify(customerRepository, times(1)).findById(2L);
        verify(customerRepository, never()).delete(any(Customer.class));
    }
}