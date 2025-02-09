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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

@RestController
@RequestMapping("/employee")
@Tag(name = "Сотрудники", description = "Управление сотрудниками")
public class EmployeeController {

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
        return employeeService.getEmployees(size, page, firstName, lastName, email, role);
    }

    @GetMapping("/{id}")
    public EmployeeResponseDTO getEmployeeById(@PathVariable long id) {
        return employeeService.getEmployeeById(id);
    }

    @PostMapping
    public EmployeeResponseDTO create(@RequestBody EmployeeCreateDTO employeeCreateDTO) {
        return employeeService.createEmployee(employeeCreateDTO);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        employeeService.deleteEmployee(id);
    }

    @PutMapping("/{id}")
    public EmployeeResponseDTO update(@PathVariable long id, @RequestBody EmployeeUpdateDTO employeeUpdateDTO) {
        return employeeService.updateEmployee(id, employeeUpdateDTO);
    }
}

