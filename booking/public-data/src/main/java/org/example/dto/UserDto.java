package org.example.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.model.enums.RoleType;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    Long id;
    @NotBlank(message = "Поле Имя пользователя не может быть пустым")
    String userName;
    @NotBlank(message = "Поле Пароль не может быть пустым")
    String password;
    @NotBlank(message = "Поле Email не может быть пустым")
    String email;

    String stringRole;
    @NotBlank(message = "Поле Роль не может быть пустым")
    RoleType role;
}

