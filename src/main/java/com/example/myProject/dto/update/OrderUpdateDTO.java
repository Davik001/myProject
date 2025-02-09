package com.example.myProject.dto.update;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO для обновления заказа")
public class OrderUpdateDTO {

    @Schema(description = "Уникальный идентификатор заказа", example = "1", required = true)
    private Long id;

    @Schema(description = "Статус заказа", example = "PROCESSING")
    private String orderStatus;

    @Schema(description = "Уникальный идентификатор клиента", example = "1")
    private Long customerId;
}
