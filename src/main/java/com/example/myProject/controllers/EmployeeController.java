package com.example.myProject.controllers;

import com.example.myProject.dto.alldtos.EmployeeDTO;
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

    @Operation(summary = "Получить список сотрудников", description = "Возвращает список сотрудников с фильтрацией и пагинацией")
    @ApiResponse(responseCode = "200", description = "Список сотрудников успешно получен")
    @GetMapping
    public Page<EmployeeDTO> getEmployees(
            @Parameter(description = "Фильтр по имени")
            @RequestParam(value = "firstName", required = false) String firstName,

            @Parameter(description = "Фильтр по фамилии")
            @RequestParam(value = "lastName", required = false) String lastName,

            @Parameter(description = "Фильтр по email")
            @RequestParam(value = "email", required = false) String email,

            @Parameter(description = "Фильтр по роли", example = "Admin")
            @RequestParam(value = "role", required = false) String role,

            @Parameter(description = "Номер страницы", example = "0")
            @RequestParam(value = "page", defaultValue = "0") int page,

            @Parameter(description = "Количество записей на странице", example = "10")
            @RequestParam(value = "size", defaultValue = "10") int size) {

        return employeeService.getEmployees(size, page, firstName, lastName, email, role);
    }

    @Operation(summary = "Получить сотрудника по ID", description = "Возвращает данные сотрудника по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Сотрудник найден"),
            @ApiResponse(responseCode = "404", description = "Сотрудник не найден")
    })
    @GetMapping("/{id}")
    public EmployeeDTO getEmployeeById(
            @Parameter(description = "ID сотрудника", required = true, example = "1")
            @PathVariable long id) {
        return employeeService.getEmployeeById(id);
    }

    @Operation(summary = "Создать сотрудника", description = "Добавляет нового сотрудника в систему")
    @ApiResponse(responseCode = "200", description = "Сотрудник успешно создан")
    @PostMapping
    public EmployeeDTO create(
            @RequestBody EmployeeDTO employeeDTO) {
        return employeeService.createEmployee(employeeDTO);
    }

    @Operation(summary = "Удалить сотрудника", description = "Удаляет сотрудника по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Сотрудник успешно удалён"),
            @ApiResponse(responseCode = "404", description = "Сотрудник не найден")
    })
    @DeleteMapping("/{id}")
    public void delete(
            @Parameter(description = "ID сотрудника", required = true, example = "1")
            @PathVariable long id) {
        employeeService.deleteEmployee(id);
    }

    @Operation(summary = "Обновить информацию о сотруднике", description = "Обновляет данные сотрудника по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Сотрудник успешно обновлён"),
            @ApiResponse(responseCode = "404", description = "Сотрудник не найден")
    })
    @PutMapping("/{id}")
    public EmployeeDTO update(
            @Parameter(description = "ID сотрудника", required = true, example = "1")
            @PathVariable long id,
            @RequestBody EmployeeDTO employeeDTO) {
        employeeDTO.setId(id);
        return employeeService.updateEmployee(employeeDTO);
    }
}
