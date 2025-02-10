package com.example.myProject.dto.update;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "DTO для обновления клиента")
public class CustomerUpdateDTO {

    @Schema(description = "Уникальный идентификатор клиента", example = "1", required = true)
    private Long id;

    @Schema(description = "Имя клиента")
    private String firstName;

    @Schema(description = "Фамилия клиента")
    private String lastName;

    @Schema(description = "Электронная почта клиента")
    private String email;

    @Schema(description = "Контактный номер телефона")
    private String phone;

    public CustomerUpdateDTO(String firstName, String lastName, String email, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
    }
}
