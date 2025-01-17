package com.example.myProject.map;

import com.example.myProject.dto.EmployeeDTO;
import com.example.myProject.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EmployeeMapper {

   // EmployeeMapper mapper = Mappers.getMapper(EmployeeMapper.class);

    EmployeeDTO toDTO(Employee employee);
    Employee toEntity(EmployeeDTO employeeDTO);

}
