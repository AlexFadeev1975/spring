package org.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDto {

    String id;

    @NotBlank(message = "Field firstName must not be blank")
    String firstName;

    @NotBlank(message = "Field lastName must not be blank")

    String lastName;

    public UserDto(String firstName, String lastName) {

        this.firstName = firstName;
        this.lastName = lastName;
    }
}
