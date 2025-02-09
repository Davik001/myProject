package com.example.myProject.service;

import com.example.myProject.dto.alldtos.CustomerDTO;
import com.example.myProject.dto.common.CustomerResponseDTO;
import com.example.myProject.dto.create.CustomerCreateDTO;
import com.example.myProject.dto.update.CustomerUpdateDTO;
import com.example.myProject.entity.Customer;
import com.example.myProject.map.CustomerMapper;
import com.example.myProject.projection.DataCustomer;
import com.example.myProject.repository.CustomerRepository;
import com.example.myProject.specifications.CustomerSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerMapper customerMapper;

    public CustomerResponseDTO createCustomer(CustomerCreateDTO customerCreateDTO) {
        Customer customer = customerMapper.toEntity(customerCreateDTO);
        customer = customerRepository.save(customer);
        return customerMapper.toDto(customer);
    }

    public void deleteCustomer(long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        customerRepository.delete(customer);
    }

    public CustomerResponseDTO updateCustomer(long id, CustomerUpdateDTO customerUpdateDTO) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        customerMapper.toEntity(customerUpdateDTO, customer);
        customer = customerRepository.save(customer);
        return customerMapper.toDto(customer);
    }

    public CustomerResponseDTO getCustomer(long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        return customerMapper.toDto(customer);
    }

    public Page<CustomerResponseDTO> getAllCustomers(DataCustomer filter, int page, int size) {
        Specification<Customer> specification = CustomerSpecifications.getSpecification(filter);
        PageRequest pageable = PageRequest.of(page, size);
        return customerRepository.findAll(specification, pageable).map(customerMapper::toDto);
    }
}

