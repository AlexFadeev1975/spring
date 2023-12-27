package org.example.test.controllers;

import io.restassured.http.ContentType;
import org.checkerframework.checker.units.qual.C;
import org.example.dto.CategoryDto;
import org.example.model.Category;
import org.example.repository.CategoryRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CategoryControllerTest extends AbstractControllerTest {

    @Autowired

    CategoryRepository categoryRepository;

    @Test
    @Order(1)
    public void shouldCreateCategory() {

        CategoryDto categoryDto = CategoryDto.builder()
                .id(null)
                .categoryName("Category 1")
                .news(new ArrayList<>())
                .build();

        given()
                .contentType(ContentType.JSON)
                .with()
                .body(categoryDto)
                .when()
                .post(ApiCollection.CREATE_CATEGORY)
                .then()
                .statusCode(200)
                .body("id", is(notNullValue()))
                .body("categoryName", hasToString("Category 1"));

    }

    @Test
    @Order(2)
    public void whenGetCategoriesThenListCategories() {
        Category category = Category.builder()
                .categoryName("Category 2")
                .news(new ArrayList<>())
                .build();

        Category category1 = Category.builder()
                .categoryName("Category 3")
                .news(new ArrayList<>())
                .build();

        categoryRepository.saveAll(List.of(category, category1));

        given()
                .contentType(ContentType.JSON)
                .when()
                .get(ApiCollection.GET_ALL_CATEGORY)
                .then()
                .statusCode(200)
                .body(".", hasSize(3));

    }

    @Test
    @Order(3)
    public void whenUpdateCategoryThenReturnCategory() {

        Category category = categoryRepository.findAll().get(0);

        CategoryDto categoryDto = CategoryDto.builder()
                .id(category.getId().toString())
                .categoryName("UpdatedCategory")
                .news(new ArrayList<>())
                .build();

        given()
                .contentType(ContentType.JSON)
                .with()
                .body(categoryDto)
                .when()
                .put(ApiCollection.UPDATE_CATEGORY)
                .then()
                .statusCode(200)
                .body("id", hasToString(category.getId().toString()))
                .body("categoryName", hasToString("UpdatedCategory"));
    }

    @Test
    @Order(4)
    public void whenDeleteCategoryThenReturnOk() {

        Category category = categoryRepository.findAll().get(1);

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", category.getId())
                .when()
                .delete(ApiCollection.DELETE_CATEGORY)
                .then()
                .body(is("Category успешно удалена"));
    }
}
