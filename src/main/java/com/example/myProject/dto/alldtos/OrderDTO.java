package com.example.myProject.dto.alldtos;

import com.example.myProject.orderStatus.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO заказов")
public class OrderDTO {

    @Schema(description = "Уникальный идентификатор заказа", example = "1", required = true)
    private Long id;

    @Schema(description = "Дата заказа", example = "2025-02-09T14:30:00")
    private LocalDateTime orderDate;

    @Schema(description = "Статус заказа", example = "NEW", required = true)
    private String orderStatus;

    @Schema(description = "Уникальный идентификатор клиента", example = "1", required = true)
    private Long customerId;

    @Schema(description = "Информация о клиенте, оформившем заказ")
    private CustomerDTO customer; // инфа про клиента. Можем использовать геттеры и сеттеры при надобности

    public OrderDTO(Long id, LocalDateTime orderDate, String orderStatus, Long customerId) {
        this.id = id;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.customerId = customerId;
    }

    public OrderDTO(Long id, LocalDateTime orderDate, String orderStatus) {
        this.id = id;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
    }
}
