package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.mapper.TaskMapper;
import org.example.model.User;
import org.example.model.dto.TaskDto;
import org.example.model.enums.TaskStatus;
import org.example.repository.TaskRepository;
import org.example.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static reactor.core.publisher.Mono.zip;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskMapper taskMapper;

    private final TaskRepository taskRepository;

    private final UserRepository userRepository;

    @Override
    public Mono<TaskDto> createTask(TaskDto dto) {

        List<String> observersNames = dto.getObserversName().contains(",")
                ? Arrays.stream(dto.getObserversName().split(",")).toList()
                : Collections.singletonList(dto.getObserversName());

        //* проверка существования в БД автора с указанным username *//
        Mono<User> author = userRepository.findByUsername(dto.getAuthorName())
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "The author is not found.")));


        //* проверка существования в БД исполнителя с указанным username *//
        Mono<User> assignee = userRepository.findByUsername(dto.getAssigneeName())
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "The assignee is not found.")));

        Mono<Set<User>> observers = userRepository.findAllByUsernameIn(observersNames).collect(Collectors.toSet());
        return zip(author, assignee, observers)
                .flatMap(tuple -> {
                    dto.setAuthor(tuple.getT1());
                    dto.setAuthorId(tuple.getT1().getId());
                    dto.setAssignee(tuple.getT2());
                    dto.setAssigneeId(tuple.getT2().getId());
                    dto.setObserverIds(tuple.getT3().stream().map(User::getId).collect(Collectors.toSet()));
                    dto.setObservers(tuple.getT3());
                    dto.setCreatedAt(Instant.now());
                    dto.setUpdatedAt(Instant.now());

                    return taskRepository.save(taskMapper.toTask(dto));
                }).map(taskMapper::toTaskDto);

    }

    @Override
    public Flux<TaskDto> getAllTasks() {

        return taskRepository.findAll()
                .flatMap(task -> {
                    Mono<User> author = userRepository.findById(task.getAuthorId());
                    Mono<User> assignee = userRepository.findById(task.getAssigneeId());
                    Mono<Set<User>> observers = userRepository.findAllByIdIn(task.getObserverIds()).collect(Collectors.toSet());

                    return zip(author, assignee, observers)
                            .map(tuple -> {
                                User athr = tuple.getT1();
                                task.setAuthor(athr);
                                User asgn = tuple.getT2();
                                task.setAssignee(asgn);
                                Set<User> susr = tuple.getT3();
                                task.setObservers(susr);
                                return task;
                            });
                }).map(taskMapper::toTaskDto);
    }

    @Override
    public Mono<TaskDto> findTaskById(String id) {

        return taskRepository.findById(id)
                .flatMap(task -> {
                    Mono<User> author = userRepository.findById(task.getAuthorId());
                    Mono<User> assignee = userRepository.findById(task.getAssigneeId());
                    Mono<Set<User>> observers = userRepository.findAllByIdIn(task.getObserverIds()).collect(Collectors.toSet());

                    return zip(author, assignee, observers)
                            .map(tuple -> {
                                User athr = tuple.getT1();
                                task.setAuthor(athr);
                                User asgn = tuple.getT2();
                                task.setAssignee(asgn);
                                Set<User> susr = tuple.getT3();
                                task.setObservers(susr);
                                return task;
                            });

                }).map(taskMapper::toTaskDto)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found")));
    }

    @Override
    public Mono<TaskDto> updateTask(TaskDto dto) {

        List<String> observersNames = dto.getObserversName().contains(",")
                ? Arrays.stream(dto.getObserversName().split(",")).toList()
                : Collections.singletonList(dto.getObserversName());

        return taskRepository.findById(dto.getId())
                .flatMap(task -> {
                    Mono<User> author = userRepository.findByUsername(dto.getAuthorName())
                            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "The author is not found.")));

                    Mono<User> assignee = userRepository.findByUsername(dto.getAssigneeName())
                            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "The assignee is not found.")));

                    Mono<Set<User>> observers = userRepository.findAllByUsernameIn(observersNames).collect(Collectors.toSet());


                    return zip(author, assignee, observers)
                            .map(tuple -> {
                                User athr = !tuple.getT1().getId().isEmpty() ? tuple.getT1() : task.getAuthor();
                                task.setAuthor(athr);
                                User asgn = !tuple.getT2().getId().isEmpty() ? tuple.getT2() : task.getAssignee();
                                task.setAssignee(asgn);
                                Set<User> susr = tuple.getT3();
                                susr.addAll(tuple.getT3());
                                task.setObservers(susr);
                                task.setUpdatedAt(Instant.now());
                                TaskStatus sts = !dto.getStatus().isEmpty() ? TaskStatus.valueOf(dto.getStatus()) : task.getStatus();
                                task.setStatus(sts);
                                String name = !dto.getName().isEmpty() ? dto.getName() : task.getName();
                                task.setName(name);
                                String dscr = !dto.getDescription().isEmpty() ? dto.getDescription() : task.getDescription();
                                task.setDescription(dscr);
                                task.setId(dto.getId());
                                return task;
                            });

                }).map(taskMapper::toTaskDto)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found")));
    }

    @Override
    public Mono<TaskDto> addObserver(TaskDto dto) {
        List<String> observersNames = dto.getObserversName().contains(",")
                ? Arrays.stream(dto.getObserversName().split(",")).toList()
                : Collections.singletonList(dto.getObserversName());

        String id = dto.getId();

        return taskRepository.findById(id)
                .flatMap(task -> {

                    Mono<Set<User>> savedObservers = userRepository.findAllByIdIn(task.getObserverIds()).collect(Collectors.toSet());

                    Mono<Set<User>> observers = userRepository.findAllByUsernameIn(observersNames).collect(Collectors.toSet());

                    return zip(savedObservers, observers)
                            .map(tuple -> {
                                Set<User> savedObs = tuple.getT1();
                                Set<User> obs = tuple.getT2();
                                savedObs.addAll(obs);
                                task.setObservers(savedObs);
                                return task;
                            });
                }).map(taskMapper::toTaskDto)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found")));

    }

    @Override
    public Mono<Void> deleteTaskById(String id) {

        return userRepository.deleteById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")));

    }
}


   

