package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.example.model.News;

import java.time.LocalDateTime;

@Data
public class CommentDto {

    String id;

    @NotBlank(message = "Comment must not be blank")
    String text;

    String userId;

    LocalDateTime createdTime;

    LocalDateTime updatedTime;

    News news;

}
