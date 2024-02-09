package org.example.services;

import org.example.model.dto.TaskDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TaskService {

    Mono<TaskDto> createTask(TaskDto dto);

    Flux<TaskDto> getAllTasks();

    Mono<TaskDto> findTaskById(String id);

    Mono<TaskDto> updateTask(TaskDto dto);

    Mono<TaskDto> addObserver(TaskDto dto);

    Mono<Void> deleteTaskById(String id);
}
