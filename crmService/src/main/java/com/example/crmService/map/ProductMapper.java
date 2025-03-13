package com.example.crmService.map;

import com.example.crmService.dto.common.ProductResponseDTO;
import com.example.crmService.dto.create.ProductCreateDTO;
import com.example.crmService.dto.update.ProductUpdateDTO;
import com.example.crmService.entity.Product;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductResponseDTO toResponseDto(Product product);
    Product toEntity(ProductCreateDTO productCreateDTO);
    Product toEntity(ProductUpdateDTO productUpdateDTO);
    List<ProductResponseDTO> toResponseDtoList(List<Product> products);
}
