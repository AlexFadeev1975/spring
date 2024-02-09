package org.example.mapper;

import lombok.RequiredArgsConstructor;
import org.example.model.Task;
import org.example.model.User;
import org.example.model.dto.TaskDto;
import org.example.model.enums.TaskStatus;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TaskMapperImpl implements TaskMapper {



    @Override
    public Task toTask(TaskDto dto) {

        Task task = new Task();

        String id = (dto.getId() == null) ? UUID.randomUUID().toString() : dto.getId();
        task.setId(id);
        task.setName(dto.getName());
        task.setDescription(dto.getDescription());

        TaskStatus status = (Objects.equals(dto.getStatus(), "DONE")) ? TaskStatus.DONE
                : (Objects.equals(dto.getStatus(), "IN_PROGRESS")) ? TaskStatus.IN_PROGRESS
                : TaskStatus.TODO;
        task.setStatus(status);

        Set<User> userSet = (dto.getObservers() == null) ? new HashSet<>()
                : new HashSet<>(dto.getObservers());
        task.setObservers(userSet);
        task.setAuthor(dto.getAuthor());
        task.setAssignee(dto.getAssignee());
        task.setAssigneeId(dto.getAssigneeId());
        task.setAuthorId(dto.getAuthorId());
        task.setObserverIds(dto.getObserverIds());
        task.setCreatedAt(dto.getCreatedAt());
        task.setUpdatedAt(dto.getUpdatedAt());


        return task;
    }

    @Override
    public TaskDto toTaskDto(Task task) {

        TaskDto dto = new TaskDto();
        dto.setId(task.getId());
        dto.setName(task.getName());
        dto.setDescription(task.getDescription());
        dto.setStatus(task.getStatus().toString());
        dto.setAuthorId(task.getAuthorId());
        dto.setAssigneeId(task.getAssigneeId());
        Set<String> ids = (task.getObservers().isEmpty()) ? new HashSet<>()
                : task.getObservers().stream().map(User::getId).collect(Collectors.toSet());
        dto.setObserverIds(ids);
        dto.setCreatedAt(task.getCreatedAt());
        dto.setUpdatedAt(task.getUpdatedAt());
        dto.setAuthor(task.getAuthor());
        dto.setAssignee(task.getAssignee());
        Set<User> userSet = (task.getObservers().isEmpty()) ? new HashSet<>()
                : new HashSet<>(task.getObservers());
        dto.setObservers(userSet);

        return dto;
    }
}
