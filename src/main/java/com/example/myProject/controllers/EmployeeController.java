package com.example.myProject.controllers;

import com.example.myProject.dto.alldtos.EmployeeDTO;
import com.example.myProject.dto.common.EmployeeResponseDTO;
import com.example.myProject.dto.create.EmployeeCreateDTO;
import com.example.myProject.dto.update.EmployeeUpdateDTO;
import com.example.myProject.repository.EmployeeRepository;
import com.example.myProject.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

@RestController
@RequestMapping("/employee")
@Tag(name = "Сотрудники", description = "Управление сотрудниками")
public class EmployeeController {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public Page<EmployeeResponseDTO> getEmployees(
            @RequestParam(value = "firstName", required = false) String firstName,
            @RequestParam(value = "lastName", required = false) String lastName,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "role", required = false) String role,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        logger.info("Fetching customers with filter: first name: {}, last name: {}, email: {}, role: {}, page: {}, size: {}",
                firstName, lastName, email, role, page, size);
            return employeeService.getEmployees(size, page, firstName, lastName, email, role);
    }


    @GetMapping("/{id}")
    public EmployeeResponseDTO getEmployeeById(@PathVariable long id) {
        logger.info("Fetching customer with ID: {}", id);
        try {
            return employeeService.getEmployeeById(id);
        } catch (Exception e) {
            logger.error("Employee with ID {} not found: {}", id, e.getMessage());
            throw e;
        }
    }

    @PostMapping
    public EmployeeResponseDTO create(@RequestBody EmployeeCreateDTO employeeCreateDTO) {
        logger.info("Creating employee : {}", employeeCreateDTO);

        try {
            return employeeService.createEmployee(employeeCreateDTO);
        } catch (Exception e) {
            logger.error("Error while creating employee {}",e.getMessage());
            throw e;
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        logger.info("Deleting employee : {}", id);
        try {
            employeeService.deleteEmployee(id);
        } catch (Exception e) {
            logger.error("Error while deleting employee with ID {}: {}", id, e.getMessage());
            throw e;
        }
    }

    @PutMapping("/{id}")
    public EmployeeResponseDTO update(@PathVariable long id, @RequestBody EmployeeUpdateDTO employeeUpdateDTO) {
        logger.info("Updating customer with ID: {}, new data: {}", id, employeeUpdateDTO);
        try {
            return employeeService.updateEmployee(id, employeeUpdateDTO);
        } catch (Exception e) {
            logger.error("Error while updating employee with ID {}: {}", id, e.getMessage());
            throw e;
        }
    }
}

