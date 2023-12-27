package org.example.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.example.model.News;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDto {
    @Pattern(regexp = "[0-9]+", message = "Field must be digit")
    String id;

    @NotBlank(message = "Field text must not be blank")
    String text;
    @Pattern(regexp = "[0-9]+", message = "Field must be digit")
    String userId;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime createdTime;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime updatedTime;

    News news;

}
