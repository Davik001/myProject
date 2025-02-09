package com.example.myProject.controllers;

import com.example.myProject.dto.alldtos.CustomerDTO;
import com.example.myProject.projection.DataCustomer;
import com.example.myProject.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
@Tag(name = "Клиенты", description = "Управление клиентами")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @Operation(summary = "Создать клиента", description = "Добавляет нового клиента в систему")
    @ApiResponse(responseCode = "200", description = "Клиент успешно создан")
    @PostMapping
    public CustomerDTO createCustomer(@RequestBody CustomerDTO customerDTO) {
        return customerService.createCustomer(customerDTO);
    }

    @Operation(summary = "Обновить клиента", description = "Обновляет информацию о клиенте по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Клиент успешно обновлён"),
            @ApiResponse(responseCode = "404", description = "Клиент не найден")
    })
    @PutMapping("/{id}")
    public CustomerDTO updateCustomer(
            @Parameter(description = "ID клиента", required = true, example = "1")
            @PathVariable Long id,
            @RequestBody CustomerDTO customerDTO) {
        return customerService.updateCustomer(id, customerDTO);
    }

    @Operation(summary = "Удалить клиента", description = "Удаляет клиента по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Клиент успешно удалён"),
            @ApiResponse(responseCode = "404", description = "Клиент не найден")
    })
    @DeleteMapping("/{id}")
    public void deleteCustomer(@Parameter(description = "ID клиента", required = true, example = "1")
                                   @PathVariable Long id) {
        customerService.deleteCustomer(id);
    }

    @Operation(summary = "Получить клиента", description = "Возвращает данные клиента по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Клиент найден"),
            @ApiResponse(responseCode = "404", description = "Клиент не найден")
    })
    @GetMapping("/{id}")
    public CustomerDTO getCustomer(
            @Parameter(description = "ID клиента", required = true, example = "1")
            @PathVariable Long id) {
        return customerService.getCustomer(id);
    }

    @Operation(summary = "Получить список клиентов", description = "Возвращает список клиентов с фильтрацией и пагинацией")
    @ApiResponse(responseCode = "200", description = "Список клиентов успешно получен")
    @GetMapping
    public Page<CustomerDTO> getCustomers(@Parameter(description = "Фильтр по имени")
                                              @RequestParam(required = false) String firstName,

                                          @Parameter(description = "Фильтр по фамилии")
                                              @RequestParam(required = false) String lastName,

                                          @Parameter(description = "Фильтр по email")
                                              @RequestParam(required = false) String email,

                                          @Parameter(description = "Фильтр по телефону")
                                              @RequestParam(required = false) String phone,

                                          @Parameter(description = "Фильтр по статусу заказа", example = "NEW")
                                              @RequestParam(required = false) String orderStatus,

                                          @Parameter(description = "Номер страницы", example = "0")
                                              @RequestParam(defaultValue = "0") int page,

                                          @Parameter(description = "Количество записей на странице", example = "10")
                                              @RequestParam(defaultValue = "10") int size) {
        DataCustomer customer = new DataCustomer(firstName, lastName, email, phone, orderStatus);

        return customerService.getAllCustomer(customer, page, size);
    }
}
