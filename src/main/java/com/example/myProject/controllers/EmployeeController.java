package com.example.myProject.controllers;

import com.example.myProject.dto.EmployeeDTO;
import com.example.myProject.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public Page<EmployeeDTO> getEmployees(
            @RequestParam(value = "firstName", required = false) String firstName,
            @RequestParam(value = "lastName", required = false) String lastName,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "role", required = false) String role,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        return employeeService.getEmployees(size, page, firstName, lastName, email, role);
    }

    // Создание нового сотрудника
    @PostMapping
    public EmployeeDTO create(@RequestBody EmployeeDTO employeeDTO) {
        return employeeService.createEmployee(employeeDTO);
    }

    // Удаление сотрудника
    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        employeeService.deleteEmployee(id);
    }

    // Обновление информации о сотруднике
    @PutMapping("/{id}")
    public EmployeeDTO update(@PathVariable long id, @RequestBody EmployeeDTO employeeDTO) {
        employeeDTO.setId(id);
        return employeeService.updateEmployee(employeeDTO);
    }

}
