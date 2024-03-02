package org.example.services;

import org.example.model.User;
import org.example.model.dto.UserDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {


    Mono<UserDto> createUser(UserDto dto);

    Flux<UserDto> getAllUsers();

    Mono<UserDto> updateUser(UserDto dto);

    Mono<UserDto> findUserById(String id);

    Mono<Void> deleteUserById(String id);

    Mono<User> findUserFromUsername (String username);
}
