package com.example.myProject.map;

import com.example.myProject.dto.alldtos.OrderDTO;
import com.example.myProject.dto.common.OrderResponseDTO;
import com.example.myProject.dto.create.OrderCreateDTO;
import com.example.myProject.dto.update.OrderUpdateDTO;
import com.example.myProject.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "customer", ignore = true)
    Order toEntity(OrderCreateDTO dto);

    @Mapping(target = "customer", ignore = true)
    Order toEntity(OrderUpdateDTO dto);

    @Mapping(target = "customer", ignore = true)
    OrderResponseDTO toDTO(Order order);
}
