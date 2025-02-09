package com.example.myProject.dto.create;

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
@Schema(description = "DTO для создания клиента")
public class CustomerCreateDTO {

    @Schema(description = "Имя клиента", required = true)
    private String firstName;

    @Schema(description = "Фамилия клиента", required = true)
    private String lastName;

    @Schema(description = "Электронная почта клиента", required = true)
    private String email;

    @Schema(description = "Контактный номер телефона")
    private String phone;
}
