package com.example.crmService.map;


import com.example.crmService.dto.common.CustomerResponseDTO;
import com.example.crmService.dto.create.CustomerCreateDTO;
import com.example.crmService.dto.update.CustomerUpdateDTO;
import com.example.crmService.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = OrderMapper.class)
public interface CustomerMapper {
    Customer toEntity(CustomerCreateDTO customerCreateDTO);
    Customer toEntity(CustomerUpdateDTO customerUpdateDTO, @MappingTarget Customer customer);
    CustomerResponseDTO toDto(Customer customer);
}
