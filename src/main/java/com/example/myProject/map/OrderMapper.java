package com.example.myProject.map;

import com.example.myProject.dto.OrderDTO;
import com.example.myProject.entity.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    Order toEntity(OrderDTO orderDTO);
    OrderDTO toDTO(Order order);
}
