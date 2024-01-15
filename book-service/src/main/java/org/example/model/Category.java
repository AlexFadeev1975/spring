package org.example.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.UUID;


@Data
@AllArgsConstructor

public class Category implements Serializable {


    @Id
    private String id;
    @Getter
    private String name;

    public Category(String name) {
        this.name = name;
        this.id = String.valueOf(UUID.randomUUID());
    }
}

