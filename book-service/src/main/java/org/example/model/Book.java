package org.example.model;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.io.Serializable;


@Data
@NoArgsConstructor

public class Book implements Serializable {

    @Id
    private String id;

    private String title;

    private String author;

    private Category category;

    public void addCategory(String category) {

        this.category = new Category(category);
    }

    public String getStringCategory() {

        return this.category.getName();
    }


}
