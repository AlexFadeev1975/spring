package org.example.mapper;

import org.example.model.User;
import org.example.model.dto.UserDto;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserMapperImpl implements UserMapper {
    @Override
    public User toUser(UserDto dto) {
        User user = new User();
        String id = (dto.getId() == null) ? UUID.randomUUID().toString() : dto.getId();
        user.setId(id);
        user.setEmail(dto.getEmail());
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        return user;
    }

    @Override
    public UserDto toUserDto(User user) {

        UserDto dto = new UserDto();

        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setUsername(user.getUsername());
        dto.setPassword(user.getPassword());
        dto.setRoles(user.getRoles());


        return dto;
    }
}
