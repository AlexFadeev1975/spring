package org.example.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(name = "category_name")
    String categoryName;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnoreProperties("category")
    List<News> news = new ArrayList<>();

    public Category(String categoryName, ArrayList<News> news) {

        this.categoryName = categoryName;
        this.news = news;
    }

    public Category(String categoryName) {
        this.categoryName = categoryName;
    }
}
