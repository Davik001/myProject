package com.example.myProject.dto.create;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO для создания заказа")
public class OrderCreateDTO {

    @Schema(description = "Статус заказа", example = "NEW", required = true)
    private String orderStatus;

    @Schema(description = "Уникальный идентификатор клиента", example = "1", required = true)
    private Long customerId;
}
