package com.example.myProject.map;

import com.example.myProject.dto.OrderDTO;
import com.example.myProject.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(target = "customer", expression = "java(new Customer(orderDTO.getCustomerId(), orderDTO.getCustomer().getFirstName()," +
            " orderDTO.getCustomer().getLastName(),orderDTO.getCustomer().getEmail(), orderDTO.getCustomer().getPhone(), null))")
    Order toEntity(OrderDTO orderDTO);

    OrderDTO toDTO(Order order);
}
