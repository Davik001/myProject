package com.example.myProject.service;

import com.example.myProject.dto.CustomerDTO;
import com.example.myProject.dto.OrderDTO;
import com.example.myProject.entity.Customer;
import com.example.myProject.entity.Order;
import com.example.myProject.map.CustomerMapper;
import com.example.myProject.map.OrderMapper;
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
    CustomerRepository customerRepository;

    @Autowired
    CustomerMapper customerMapper;

    @Autowired
    OrderMapper orderMapper;

    // создать
    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        // Преобразуем DTO клиента в сущность
        Customer customer = customerMapper.toEntity(customerDTO);

        // Обрабатываем заказы, если они есть
        if (customerDTO.getOrders() != null) {
            for (OrderDTO orderDTO : customerDTO.getOrders()) {
                // Преобразуем каждый OrderDTO в сущность Order
                Order order = orderMapper.toEntity(orderDTO);

                // Устанавливаем связь между клиентом и заказом
                customer.addOrder(order);  // Метод addOrder на сущности Customer
            }
        }

        // Сохраняем клиента и связанные заказы
        customer = customerRepository.save(customer);

        // Преобразуем обратно в DTO и возвращаем
        return customerMapper.toDto(customer);
    }

    // удалить
    public void deleteCustomer(long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        customerRepository.delete(customer);
    }

    // обновить
    public CustomerDTO updateCustomer(long id, CustomerDTO customerDTO){
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        customer.setFirstName(customerDTO.getFirstName());
        customer.setLastName(customerDTO.getLastName());
        customer.setEmail(customerDTO.getEmail());
        customer.setPhone(customerDTO.getPhone());
        customer = customerRepository.save(customer);
        return customerMapper.toDto(customer);
    }

    // чтение
    public CustomerDTO getCustomer(long id){
        Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Customer not found"));
        return customerMapper.toDto(customer);}

    public Page<CustomerDTO> getAllCustomer(DataCustomer filter, int page, int size){
        Specification<Customer> specification = CustomerSpecifications.getSpecification(filter);

        PageRequest pageable = PageRequest.of(page, size);
        Page<Customer> customersPage = customerRepository.findAll(specification, pageable);

        // Преобразование в DTO
        return customersPage.map(customerMapper::toDto);
    }
}
