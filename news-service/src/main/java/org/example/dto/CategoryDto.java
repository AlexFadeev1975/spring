package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.example.model.News;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDto {

    @Pattern(regexp = "[0-9]+", message = "Field must be digit")
    private String id;

    @NotBlank(message = "Field CategoryName must not be blank")
    private String categoryName;

    List<News> news;
}
