package org.example.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.example.model.Category;

import java.io.Serializable;


@Data
@NoArgsConstructor

public class BookDto implements Serializable {

    private String id;
    @NotBlank(message = "Field Title must not be blank")
    private String title;

    @NotBlank(message = "Field Author must not be blank")
    private String author;

    @NotBlank(message = "Field Category must not be blank")
    @JsonProperty("category")
    private String category;

    public void addCategory(Category category) {
        this.category = category.getName();
    }

}
