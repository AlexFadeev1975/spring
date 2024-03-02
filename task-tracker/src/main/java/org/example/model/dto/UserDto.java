package org.example.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.model.enums.RoleType;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Data
@Document
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private String id;

    private String username;

    private String email;

    private String password;

    private String role;

    private Set<RoleType> roles;


}
