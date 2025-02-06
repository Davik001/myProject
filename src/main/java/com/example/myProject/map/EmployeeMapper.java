package com.example.myProject.map;

import com.example.myProject.dto.alldtos.EmployeeDTO;
import com.example.myProject.entity.Employee;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

   // EmployeeMapper mapper = Mappers.getMapper(EmployeeMapper.class);

    EmployeeDTO toDTO(Employee employee);
    Employee toEntity(EmployeeDTO employeeDTO);

}
