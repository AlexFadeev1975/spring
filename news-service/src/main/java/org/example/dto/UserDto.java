package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.example.model.enums.RoleType;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    @Pattern(regexp = "[0-9]+", message = "Field id must be digit")
    String id;

    @NotBlank(message = "Field firstName must not be blank")
    String firstName;

    @NotBlank(message = "Field lastName must not be blank")
    String lastName;

    String email;

    String password;

    String role;


    public UserDto(String firstName, String lastName) {

        this.firstName = firstName;
        this.lastName = lastName;
    }
}
