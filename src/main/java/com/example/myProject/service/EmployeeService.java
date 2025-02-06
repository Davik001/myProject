package com.example.myProject.service;

import com.example.myProject.dto.alldtos.EmployeeDTO;
import com.example.myProject.entity.Employee;
import com.example.myProject.map.EmployeeMapper;
import com.example.myProject.repository.EmployeeRepository;
import com.example.myProject.specifications.EmployeeSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class EmployeeService {

    @Autowired
    private EmployeeRepository empRepository;

    @Autowired
    private EmployeeMapper empMapper;

    // создание сотрудника
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        if(empRepository.findByEmail(employeeDTO.getEmail()) != null){
            throw new RuntimeException("Email already exists");
        }

        Employee emp = empMapper.toEntity(employeeDTO);
        return empMapper.toDTO(empRepository.save(emp));
    }

    // удалить
    public void deleteEmployee(long id) {
        if(!empRepository.findById(id).isEmpty()){
            throw new RuntimeException("Employee not found");
        }
        empRepository.deleteById(id);
    }

    // обновить
    public EmployeeDTO updateEmployee(EmployeeDTO employeeDTO) {
        Optional<Employee> existingEmployee = empRepository.findById(employeeDTO.getId());
        if(!existingEmployee.isPresent()){
            throw new RuntimeException("Employee not found");
        }

        if (empRepository.findByEmail(employeeDTO.getEmail()) != null && !employeeDTO.getEmail().equals(existingEmployee.get().getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        Employee emp = existingEmployee.get();
        emp.setFirstName(employeeDTO.getFirstName());
        emp.setLastName(employeeDTO.getLastName());
        emp.setEmail(employeeDTO.getEmail());
        emp.setPassword(employeeDTO.getPassword());
        emp.setRole(employeeDTO.getRole());
        return empMapper.toDTO(empRepository.save(emp));
    }

    // читаем данные
    public Page<EmployeeDTO> getEmployees(int size, int page, String firstName, String lastName, String email, String role) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("firstName"), Sort.Order.asc("role")));

        // Строим Specification для фильтрации
        Specification<Employee> spec = Specification.where(EmployeeSpecifications.hasFirstName(firstName))
                .and(EmployeeSpecifications.hasLastName(lastName))
                .and(EmployeeSpecifications.hasEmail(email))
                .and(EmployeeSpecifications.hasRole(role));

        Page<Employee> employees = empRepository.findAll(spec, pageable);

        // Преобразуем в DTO
        return employees.map(empMapper::toDTO);
    }


}
