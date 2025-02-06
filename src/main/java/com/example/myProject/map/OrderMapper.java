package com.example.myProject.map;

import com.example.myProject.dto.alldtos.OrderDTO;
import com.example.myProject.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "customer", ignore = true)
    Order toEntity(OrderDTO orderDTO);

    @Mapping(target = "customer", ignore = true)
    OrderDTO toDTO(Order order);
}
