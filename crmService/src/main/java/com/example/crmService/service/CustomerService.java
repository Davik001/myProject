package com.example.crmService.service;


import com.example.crmService.dto.common.CustomerResponseDTO;
import com.example.crmService.dto.create.CustomerCreateDTO;
import com.example.crmService.dto.update.CustomerUpdateDTO;
import com.example.crmService.entity.Customer;
import com.example.crmService.map.CustomerMapper;
import com.example.crmService.projection.DataCustomer;
import com.example.crmService.repository.CustomerRepository;
import com.example.crmService.specifications.CustomerSpecifications;
import jakarta.persistence.EntityNotFoundException;
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
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));
        customerRepository.delete(customer);
    }

    public CustomerResponseDTO updateCustomer(long id, CustomerUpdateDTO customerUpdateDTO) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));
        customerMapper.toEntity(customerUpdateDTO, customer);
        customer = customerRepository.save(customer);
        return customerMapper.toDto(customer);
    }

    public CustomerResponseDTO getCustomer(long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));
        return customerMapper.toDto(customer);
    }

    public Page<CustomerResponseDTO> getAllCustomers(DataCustomer filter, int page, int size) {
        Specification<Customer> specification = CustomerSpecifications.getSpecification(filter);
        PageRequest pageable = PageRequest.of(page, size);
        return customerRepository.findAll(specification, pageable).map(customerMapper::toDto);
    }
}

