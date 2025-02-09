package com.example.myProject.dto.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO для ответа с информацией о заказе")
public class OrderResponseDTO {

    @Schema(description = "Уникальный идентификатор заказа", example = "1", required = true)
    private Long id;

    @Schema(description = "Дата заказа", example = "2025-02-09T14:30:00", required = true)
    private LocalDateTime orderDate;

    @Schema(description = "Статус заказа", example = "NEW", required = true)
    private String orderStatus;

    @Schema(description = "Уникальный идентификатор клиента", example = "1", required = true)
    private Long customerId;

    @Schema(description = "Информация о клиенте, оформившем заказ")
    private CustomerResponseDTO customer;
}
