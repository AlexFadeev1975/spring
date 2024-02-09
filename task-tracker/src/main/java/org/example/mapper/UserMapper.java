package org.example.mapper;

import org.example.model.User;
import org.example.model.dto.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(UserDto dto);

    UserDto toUserDto(User user);
}
