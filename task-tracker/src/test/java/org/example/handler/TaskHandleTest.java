package org.example.handler;

import org.example.model.dto.TaskDto;
import org.example.model.dto.UserDto;
import org.example.model.enums.RoleType;
import org.example.repository.TaskRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Set;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TaskHandleTest extends TaskTrackerTest{

    @Autowired
    private WebTestClient webTestClient;

   @Autowired
    private TaskRepository taskRepository;
    static UserDto userDto;
    static UserDto userDto1;
     static TaskDto taskDto;



    @Order(0)
    @Test
    public void createTask() {

       taskRepository.deleteAll().subscribe();

       String pass = passwordEncoder.encode("123");

        Flux<UserDto> userDtoFlux = webTestClient.post()
                .uri(ApiCollection.CREATE_USER)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(new UserDto("1", "user test", "email@email.ru", pass, "ROLE_MANAGER", Set.of(RoleType.ROLE_MANAGER))))
                .exchange()
                .expectStatus().isOk()
                .returnResult(UserDto.class).getResponseBody()
                .log();
        userDtoFlux.next().subscribe(user -> {
            userDto = user;
        });

        TaskDto dto = new TaskDto();
        dto.setName("task test");
        dto.setDescription("todo 1");
        dto.setAuthorName("user test");
        dto.setAssigneeName("user test");
        dto.setObserversName("user test");


        Flux<TaskDto> taskDtoFlux = webTestClient.post()
                .uri(ApiCollection.CREATE_TASK)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(httpHeaders -> httpHeaders.setBasicAuth("user test", "123"))
                .body(BodyInserters.fromValue(dto))
                .exchange()
                .expectStatus().isOk()
                .returnResult(TaskDto.class).getResponseBody()
                .log();
        taskDtoFlux.next().subscribe(task -> {
            taskDto = task;
            Assertions.assertNotNull(taskDto);
        });

    }

    @Order(1)
    @Test
    public void getTaskById() {
        Flux<TaskDto> taskDtoFlux = webTestClient.get()
                .uri(ApiCollection.FIND_TASK, taskDto.getId())
                .headers(httpHeaders -> httpHeaders.setBasicAuth("user test", "123"))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(TaskDto.class).getResponseBody()
                .log();
        StepVerifier.create(taskDtoFlux)
                .expectSubscription()
                .expectNextMatches(taskDto -> taskDto.getName().equals("task test"))
                .verifyComplete();
    }

    @Test
    @Order(3)
    public void updateTask() {

        TaskDto dto = new TaskDto();
        dto.setId(taskDto.getId());
        dto.setName("task new");
        dto.setDescription("todo new");
        dto.setAuthorName("user test");
        dto.setAssigneeName("user test");
        dto.setObserversName("user test");
        dto.setStatus("TODO");

        Flux<TaskDto> taskDtoFlux = webTestClient.put()
                .uri(ApiCollection.UPDATE_TASK)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(httpHeaders -> httpHeaders.setBasicAuth("user test", "123"))
                .body(BodyInserters.fromValue(dto))
                .exchange()
                .returnResult(TaskDto.class).getResponseBody()
                .log();
        StepVerifier.create(taskDtoFlux)
                .expectSubscription()
                .expectNextMatches(task -> task.getName().equals("task new"))
                .verifyComplete();
    }

    @Test
    @Order(4)
    public void getAllTasks() {
        Flux<TaskDto> taskDtoFlux = webTestClient.get()
                .uri(ApiCollection.GET_TASKS)
                .accept(MediaType.APPLICATION_JSON)
                .headers(httpHeaders -> httpHeaders.setBasicAuth("user test", "123"))
                .exchange()
                .returnResult(TaskDto.class).getResponseBody()
                .log();
        StepVerifier.create(taskDtoFlux)
                .expectSubscription()
                .expectNextCount(1L)
                .verifyComplete();
    }

    @Test
    @Order(2)
    public void addObserver() {


        Flux<UserDto> userDtoFlux = webTestClient.post()
                .uri(ApiCollection.CREATE_USER)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(new UserDto("2", "user 2test", "my@email.ru","123", "ROLE_USER",  Set.of(RoleType.ROLE_USER))))
                .headers(httpHeaders -> httpHeaders.setBasicAuth("user test", "123"))
                .exchange()
                .expectStatus().isOk()
                .returnResult(UserDto.class).getResponseBody()
                .log();
        userDtoFlux.subscribe(user -> {
            userDto1 = user;
        });

        TaskDto dto = new TaskDto();
        dto.setId(taskDto.getId());
        dto.setObserversName("user 2test");

        Flux<TaskDto> taskDtoFlux = webTestClient.put()
                .uri(ApiCollection.ADD_OBSERVER)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(dto))
                .headers(httpHeaders -> httpHeaders.setBasicAuth("user test", "123"))
                .exchange()
                .returnResult(TaskDto.class).getResponseBody()
                .log();

        StepVerifier.create(taskDtoFlux)
                .expectSubscription()
                .expectNextMatches(task ->
                    task.getObservers().size() == 2)
                .verifyComplete();
    }

    @Test
    @Order(5)
    public void deleteTask() {
        Flux<Void> taskDtoFlux = webTestClient.delete()
                .uri(ApiCollection.DELETE_TASK, taskDto.getId())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .returnResult(Void.class).getResponseBody();
        StepVerifier.create(taskDtoFlux)
                .expectSubscription()
                .verifyComplete();


    }
}
