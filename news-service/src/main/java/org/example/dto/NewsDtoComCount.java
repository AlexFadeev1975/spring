package org.example.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.model.Category;

import java.time.LocalDateTime;

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
