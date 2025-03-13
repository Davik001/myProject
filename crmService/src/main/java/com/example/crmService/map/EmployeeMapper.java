package com.example.crmService.map;


import com.example.crmService.dto.common.EmployeeResponseDTO;
import com.example.crmService.dto.create.EmployeeCreateDTO;
import com.example.crmService.dto.update.EmployeeUpdateDTO;
import com.example.crmService.entity.Employee;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    EmployeeResponseDTO toResponseDTO(Employee employee);
    Employee toEntity(EmployeeCreateDTO employeeCreateDTO);
    Employee toEntity(EmployeeUpdateDTO employeeUpdateDTO);
}