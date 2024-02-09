package org.example.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.model.dto.UserDto;
import org.example.services.UserServiceImpl;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserHandler {

    private final UserServiceImpl userService;

    public Mono<ServerResponse> createUser(ServerRequest serverRequest) {

        return serverRequest.bodyToMono(UserDto.class)
                .flatMap(user ->
                        ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(userService.createUser(user), UserDto.class));
    }

    public Mono<ServerResponse> getAllUsers(ServerRequest serverRequest) {

        Flux<UserDto> users = userService.getAllUsers();

        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(users, UserDto.class);
    }


    public Mono<ServerResponse> updateUser(ServerRequest serverRequest) {

        return serverRequest.bodyToMono(UserDto.class)
                .flatMap(dto ->
                        ServerResponse.ok()
                                .contentType(APPLICATION_JSON)
                                .body(userService.updateUser(dto), UserDto.class));
    }


    public Mono<ServerResponse> findUserById(ServerRequest serverRequest) {

        String id = serverRequest.pathVariable("id");

        return userService.findUserById(id)
                .flatMap(userDto -> ServerResponse.ok().contentType(APPLICATION_JSON).bodyValue(userDto));


    }

    public Mono<ServerResponse> deleteUserById(ServerRequest serverRequest) {

        String id = serverRequest.pathVariable("id");

        return userService.findUserById(id)
                .flatMap(user -> ServerResponse.noContent().build(userService.deleteUserById(id)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }


}
