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
@Schema(description = "DTO для обновления сотрудника")
public class EmployeeUpdateDTO {

    @Schema(description = "Уникальный идентификатор сотрудника", example = "1", required = true)
    private long id;

    @Schema(description = "Имя сотрудника")
    private String firstName;

    @Schema(description = "Фамилия сотрудника")
    private String lastName;

    @Schema(description = "Электронная почта сотрудника")
    private String email;

    @Schema(description = "Пароль сотрудника")
    private String password;

    @Schema(description = "Роль сотрудника", example = "Admin")
    private String role;
}

