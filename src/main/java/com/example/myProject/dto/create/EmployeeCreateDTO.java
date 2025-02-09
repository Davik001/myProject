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
@Schema(description = "DTO для создания сотрудника")
public class EmployeeCreateDTO {

    @Schema(description = "Имя сотрудника", required = true)
    private String firstName;

    @Schema(description = "Фамилия сотрудника", required = true)
    private String lastName;

    @Schema(description = "Электронная почта сотрудника", required = true)
    private String email;

    @Schema(description = "Пароль сотрудника", required = true)
    private String password;

    @Schema(description = "Роль сотрудника", example = "Admin", required = true)
    private String role;
}
