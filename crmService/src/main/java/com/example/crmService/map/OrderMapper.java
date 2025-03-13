package com.example.crmService.map;


import com.example.crmService.dto.common.OrderResponseDTO;
import com.example.crmService.dto.create.OrderCreateDTO;
import com.example.crmService.dto.update.OrderUpdateDTO;
import com.example.crmService.entity.Order;
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
