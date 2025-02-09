package com.example.myProject.dto.alldtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO продуктов")
public class ProductDTO {

    @Schema(description = "Уникальный идентификатор продуктов", example = "1", required = true)
    private Long id;

    @Schema(description = "Название продукта", required = true)
    private String name;

    @Schema(description = "Описание продукта")
    private String description;

    @Schema(description = "Цена", required = true)
    private BigDecimal price;
}