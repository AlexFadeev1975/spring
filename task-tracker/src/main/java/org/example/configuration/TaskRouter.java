package org.example.configuration;

import org.example.handler.TaskHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class TaskRouter {
    @Bean
    public RouterFunction<ServerResponse> routeTask(TaskHandler taskHandler) {

        return RouterFunctions.route()
                .path("/tasks/api/v1", builder -> builder
                        .POST("/create", accept(APPLICATION_JSON), taskHandler::createTask)
                        .GET("/tasks", accept(APPLICATION_JSON), taskHandler::findAllTasks)
                        .GET("/{id}", accept(APPLICATION_JSON), taskHandler::findTaskById)
                        .PUT("/update", accept(APPLICATION_JSON), taskHandler::updateTask)
                        .PUT("/observer", accept(APPLICATION_JSON), taskHandler::addObserver)
                        .DELETE("/{id}", accept(APPLICATION_JSON), taskHandler::deleteTask))
                .build();
    }


}
