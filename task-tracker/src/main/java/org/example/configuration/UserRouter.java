package org.example.configuration;

import org.example.handler.UserHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class UserRouter {
    @Bean
    public RouterFunction<ServerResponse> routeUser(UserHandler userHandler) {

        return RouterFunctions.route()
                .path("/users/api/v1", builder -> builder
                        .POST("/create", accept(APPLICATION_JSON), userHandler::createUser)
                        .GET("/users", accept(APPLICATION_JSON), userHandler::getAllUsers)
                        .GET("/{id}", accept(APPLICATION_JSON), userHandler::findUserById)
                        .PUT("/update", accept(APPLICATION_JSON), userHandler::updateUser)
                        .DELETE("/{id}", accept(APPLICATION_JSON), userHandler::deleteUserById)).build();


    }
}
