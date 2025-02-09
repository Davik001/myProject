package com.example.myProject.map;

import com.example.myProject.dto.create.ProductCreateDTO;
import com.example.myProject.dto.common.ProductResponseDTO;
import com.example.myProject.dto.update.ProductUpdateDTO;
import com.example.myProject.entity.Product;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductResponseDTO toResponseDto(Product product);
    Product toEntity(ProductCreateDTO productCreateDTO);
    Product toEntity(ProductUpdateDTO productUpdateDTO);
    List<ProductResponseDTO> toResponseDtoList(List<Product> products);
}
