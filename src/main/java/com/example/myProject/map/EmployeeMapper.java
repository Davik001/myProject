package com.example.myProject.map;

import com.example.myProject.dto.alldtos.EmployeeDTO;
import com.example.myProject.dto.common.EmployeeResponseDTO;
import com.example.myProject.dto.create.EmployeeCreateDTO;
import com.example.myProject.dto.update.EmployeeUpdateDTO;
import com.example.myProject.entity.Employee;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    EmployeeResponseDTO toResponseDTO(Employee employee);
    Employee toEntity(EmployeeCreateDTO employeeCreateDTO);
    Employee toEntity(EmployeeUpdateDTO employeeUpdateDTO);
}