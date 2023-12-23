package org.example.dto;

import lombok.Data;
import org.example.model.Category;

@Data
public class NewsSearchDto {


    String categoryName;

    Category category;

    String author;
}
