package com.example.myProject.map;

import com.example.myProject.dto.alldtos.CustomerDTO;
import com.example.myProject.dto.common.CustomerResponseDTO;
import com.example.myProject.dto.create.CustomerCreateDTO;
import com.example.myProject.dto.update.CustomerUpdateDTO;
import com.example.myProject.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = OrderMapper.class)
public interface CustomerMapper {
    Customer toEntity(CustomerCreateDTO customerCreateDTO);
    Customer toEntity(CustomerUpdateDTO customerUpdateDTO, @MappingTarget Customer customer);
    CustomerResponseDTO toDto(Customer customer);
}
