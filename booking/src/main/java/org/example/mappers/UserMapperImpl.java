package org.example.mappers;

import org.example.dto.UserDto;
import org.example.model.User;
import org.example.model.enums.RoleType;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public class UserMapperImpl implements UserMapper{
    @Override
    public User userDtoToUser(UserDto dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setUserName(dto.getUserName());
        user.setPassword(dto.getPassword());
        user.setEmail(dto.getEmail());
        user.setRole(RoleType.valueOf(dto.getStringRole()));
        return user;
    }

    @Override
    public UserDto userToUserDto(User user) {

        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUserName(user.getUserName());
        dto.setPassword(user.getPassword());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        return dto;
    }
}
