package org.example.mappers;

import org.example.dto.UserDto;
import org.example.model.User;
import org.example.model.enums.RoleType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)

public interface UserMapper {
    @Mapping(target = "role", source = "stringRole", qualifiedByName = "mapRole")
    User userDtoToUser(UserDto dto);

    UserDto userToUserDto(User user);


    @Named("mapRole")
    default RoleType mapToRole(String stringRole) {

        return RoleType.valueOf(stringRole);
    }
}
