package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.example.model.News;

import java.util.List;

@Data
public class CategoryDto {

    String id;

    @NotBlank
    String categoryName;

    List<News> news;
}
