package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.mapper.UserMapper;
import org.example.model.User;
import org.example.model.dto.UserDto;
import org.example.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @Override
    public Mono<UserDto> createUser(UserDto dto) {

        User user = userMapper.toUser(dto);
        return Mono.just(dto)
                .flatMap(usr -> userRepository.findByUsername(usr.getUsername())
                        .hasElement()
                        .flatMap(isUserExist -> isUserExist
                                ? Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already exist"))
                                : userRepository.save(user)
                                .map(userMapper::toUserDto)));
    }

    @Override
    public Mono<UserDto> updateUser(UserDto dto) {


        return Mono.just(dto)
                .flatMap(d -> userRepository.findById(d.getId())
                        .flatMap(user -> {
                            user.setUsername(dto.getUsername());
                            user.setEmail(dto.getEmail());
                            return userRepository.save(user)
                                    .map(userMapper::toUserDto);
                        }).switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"))));


    }

    @Override
    public Mono<UserDto> findUserById(String id) {

        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")))
                .map(userMapper::toUserDto);
    }

    @Override
    public Mono<Void> deleteUserById(String id) {

        return userRepository.deleteById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")));

    }

    @Override
    public Flux<UserDto> getAllUsers() {

        return userRepository.findAll()
                .map(userMapper::toUserDto);
    }
}
