package org.example.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.example.model.Category;
import org.example.model.Comment;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class NewsDto {

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

    String categoryName;

    Category category;

    List<Comment> comments;


}
