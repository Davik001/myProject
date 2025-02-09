package com.example.myProject.service;

import com.example.myProject.dto.alldtos.EmployeeDTO;
import com.example.myProject.dto.common.EmployeeResponseDTO;
import com.example.myProject.dto.create.EmployeeCreateDTO;
import com.example.myProject.dto.update.EmployeeUpdateDTO;
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
    public EmployeeResponseDTO createEmployee(EmployeeCreateDTO employeeCreateDTO) {
        if(empRepository.findByEmail(employeeCreateDTO.getEmail()) != null){
            throw new RuntimeException("Email already exists");
        }

        Employee emp = empMapper.toEntity(employeeCreateDTO);
        return empMapper.toResponseDTO(empRepository.save(emp));
    }

    // удалить
    public void deleteEmployee(long id) {
        if(empRepository.findById(id).isEmpty()){
            throw new RuntimeException("Employee not found");
        }
        empRepository.deleteById(id);
    }

    // обновить
    public EmployeeResponseDTO updateEmployee(long id, EmployeeUpdateDTO employeeUpdateDTO) {
        Employee existingEmployee = empRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        if (empRepository.findByEmail(employeeUpdateDTO.getEmail()) != null &&
                !employeeUpdateDTO.getEmail().equals(existingEmployee.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        existingEmployee.setFirstName(employeeUpdateDTO.getFirstName());
        existingEmployee.setLastName(employeeUpdateDTO.getLastName());
        existingEmployee.setEmail(employeeUpdateDTO.getEmail());
        existingEmployee.setPassword(employeeUpdateDTO.getPassword());
        existingEmployee.setRole(employeeUpdateDTO.getRole());

        return empMapper.toResponseDTO(empRepository.save(existingEmployee));
    }

    // читаем данные
    public Page<EmployeeResponseDTO> getEmployees(int size, int page, String firstName, String lastName, String email, String role) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("firstName"), Sort.Order.asc("role")));

        Specification<Employee> spec = Specification.where(EmployeeSpecifications.hasFirstName(firstName))
                .and(EmployeeSpecifications.hasLastName(lastName))
                .and(EmployeeSpecifications.hasEmail(email))
                .and(EmployeeSpecifications.hasRole(role));

        Page<Employee> employees = empRepository.findAll(spec, pageable);
        return employees.map(empMapper::toResponseDTO);
    }

    public EmployeeResponseDTO getEmployeeById(long id) {
        return empRepository.findById(id)
                .map(empMapper::toResponseDTO)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
    }
}
