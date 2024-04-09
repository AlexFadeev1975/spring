package org.example.mappers;

import org.example.dto.UserDto;
import org.example.model.User;
import org.example.model.enums.RoleType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Mapper

public interface UserMapper {

    User userDtoToUser(UserDto dto);

    UserDto userToUserDto(User user);

    }

