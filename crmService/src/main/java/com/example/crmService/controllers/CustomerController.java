package com.example.crmService.controllers;


import com.example.crmService.dto.common.CustomerResponseDTO;
import com.example.crmService.dto.create.CustomerCreateDTO;
import com.example.crmService.dto.update.CustomerUpdateDTO;
import com.example.crmService.entity.Customer;
import com.example.crmService.exception.customException.ResourceNotFoundException;
import com.example.crmService.projection.DataCustomer;
import com.example.crmService.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
@Tag(name = "Клиенты", description = "Управление клиентами")
public class CustomerController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private CustomerService customerService;

    @PostMapping
    public CustomerResponseDTO createCustomer(@RequestBody CustomerCreateDTO customerCreateDTO) {
        logger.info("Creating customer with data: {}", customerCreateDTO);
        try {
            return customerService.createCustomer(customerCreateDTO);
        } catch (Exception e) {
            logger.error("Error while creating customer: {}", e.getMessage(), e);
            throw e;
        }
    }

    @PutMapping("/{id}")
    public CustomerResponseDTO updateCustomer(@PathVariable Long id, @RequestBody CustomerUpdateDTO customerUpdateDTO) {
        logger.info("Updating customer with ID: {}, new data: {}", id, customerUpdateDTO);
        try {
            return customerService.updateCustomer(id, customerUpdateDTO);
        } catch (Exception e) {
            logger.error("Error while updating customer with ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @DeleteMapping("/{id}")
    public void deleteCustomer(@PathVariable Long id) {
        logger.info("Deleting customer with ID: {}", id);
        try {
            customerService.deleteCustomer(id);
        } catch (Exception e) {
            logger.error("Error while deleting customer with ID {}: {}", id, e.getMessage());
            throw e;
        }
    }

    @GetMapping("/{id}")
    public CustomerResponseDTO getCustomer(@PathVariable Long id) {
        logger.debug("Fetching customer with ID: {}", id);
        CustomerResponseDTO customer = customerService.getCustomer(id);

        if (customer == null) {
            logger.info("Customer with ID {} not found.", id);
            throw new ResourceNotFoundException("Customer with ID " + id + " not found.");
        }

        logger.info("Customer with ID {} found.", id);
        return customer;
    }

    @GetMapping
    public Page<CustomerResponseDTO> getCustomers(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String orderStatus,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        logger.info("Fetching customers with filter: firstName={}, lastName={}, email={}, phone={}, orderStatus={}, page={}, size={}",
                firstName, lastName, email, phone, orderStatus, page, size);
        DataCustomer filter = new DataCustomer(firstName, lastName, email, phone, orderStatus);
        return customerService.getAllCustomers(filter, page, size);
    }

    // для получения имейла
    @GetMapping("/{id}/email")
    public ResponseEntity<String> getCustomersEmail(@PathVariable Long id) {
        return customerService.getCustomerEmail(id);
    }
}

