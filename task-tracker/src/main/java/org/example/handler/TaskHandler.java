package org.example.handler;

import lombok.RequiredArgsConstructor;
import org.example.exceptions.TaskValidator;
import org.example.model.dto.TaskDto;
import org.example.services.TaskServiceImpl;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
@RequiredArgsConstructor
public class TaskHandler {

    private final TaskServiceImpl taskService;

    private final TaskValidator validator;

    public Mono<ServerResponse> createTask(ServerRequest serverRequest) {

        return serverRequest.bodyToMono(TaskDto.class).doOnNext(this::validate)
                .flatMap(dto -> {
                    return ServerResponse.ok()
                            .contentType(APPLICATION_JSON)
                            .body(taskService.createTask(dto), TaskDto.class);
                });
    }

    public Mono<ServerResponse> findAllTasks(ServerRequest serverRequest) {

        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(taskService.getAllTasks(), TaskDto.class);
    }

    public Mono<ServerResponse> findTaskById(ServerRequest serverRequest) {

        String id = serverRequest.pathVariable("id");

        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(taskService.findTaskById(id), TaskDto.class);
    }

    public Mono<ServerResponse> updateTask(ServerRequest serverRequest) {

        return serverRequest.bodyToMono(TaskDto.class).doOnNext(this::validate)
                .flatMap(task -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .body(taskService.updateTask(task), TaskDto.class));
    }

    public Mono<ServerResponse> addObserver(ServerRequest serverRequest) {

        return serverRequest.bodyToMono(TaskDto.class)
                .flatMap(taskDto ->
                        ServerResponse.ok()
                                .contentType(APPLICATION_JSON)
                                .body(taskService.addObserver(taskDto), TaskDto.class));
    }

    public Mono<ServerResponse> deleteTask(ServerRequest serverRequest) {

        String id = serverRequest.pathVariable("id");

        return taskService.findTaskById(id)
                .flatMap(task -> ServerResponse.noContent().build(taskService.deleteTaskById(id)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public void validate(TaskDto task) {
        Errors errors = new BeanPropertyBindingResult(task, TaskDto.class.getName());
        validator.validate(task, errors);
        if (errors.hasErrors()) {
            throw new ServerWebInputException(errors.toString());
        }
    }
}



