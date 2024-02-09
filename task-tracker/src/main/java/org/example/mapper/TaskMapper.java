package org.example.mapper;

import org.example.model.Task;
import org.example.model.dto.TaskDto;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface TaskMapper {
    Task toTask(TaskDto dto);

    TaskDto toTaskDto(Task task);


}
