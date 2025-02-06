package com.example.myProject.map;

import com.example.myProject.dto.alldtos.CustomerDTO;
import com.example.myProject.entity.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",uses = OrderMapper.class  )
public interface CustomerMapper {
    Customer toEntity(CustomerDTO customerDTO);
    CustomerDTO toDto(Customer customer);
}
