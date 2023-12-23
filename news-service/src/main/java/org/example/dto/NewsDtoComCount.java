package org.example.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.example.model.Category;
import org.example.model.Comment;
import org.hibernate.annotations.Comments;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewsDtoComCount {

    String id;

    String text;

    String userId;

    LocalDateTime createdTime;

    LocalDateTime updatedTime;

    String categoryName;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    Category category;

    Integer comments;


}
