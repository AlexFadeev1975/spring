package org.example.test.controllers;

import io.restassured.http.ContentType;
import jakarta.validation.constraints.Negative;
import org.example.dto.NewsDto;
import org.example.model.Category;
import org.example.model.News;
import org.example.repository.NewsRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class NewsControllerTest extends AbstractControllerTest {

    @Autowired
    NewsRepository newsRepository;


    @Test
    @Order(1)
    public void shouldCreateNews() {

        NewsDto newsDto = NewsDto.builder()
                .id(null)
                .text("News 1")
                .categoryName("Category 1")
                .createdTime(null)
                .updatedTime(null)
                .build();

        Category category = new Category("Category 1");

        given()
                .contentType(ContentType.JSON)
                .with()
                .body(newsDto)
                .header("userId", 1)
                .when()
                .post(ApiCollection.CREATE_NEWS)
                .then()
                .statusCode(200)
                .body("id", is(notNullValue()))
                .body("text", hasToString("News 1"))
                .body("createdTime", matchesPattern("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}"))
                .body("category.categoryName", hasToString("Category 1"))
                .body("userId", hasToString(String.valueOf(1)));

    }

    @Test
    @Order(2)
    public void whenGetAllNewsThenReturnListNews() {

        News news = News.builder()
                .text("News 2")
                .createdTime(LocalDateTime.now())
                .updatedTime(LocalDateTime.now())
                .category(new Category("Category 2"))
                .comments(new ArrayList<>())
                .userId(1L)
                .build();

        News news1 = News.builder()
                .text("News 3")
                .createdTime(LocalDateTime.now())
                .updatedTime(LocalDateTime.now())
                .category(new Category("Category 2"))
                .comments(new ArrayList<>())
                .userId(1L)
                .build();
        newsRepository.save(news);
        newsRepository.save(news1);

        given()
                .contentType(ContentType.JSON)
                .when()
                .get(ApiCollection.GET_ALL_NEWS)
                .then()
                .statusCode(200)
                .body(".", hasSize(3));
    }


    @Test
    @Order(3)
    public void whenUpdateNewsReturnNews() {

        News news = newsRepository.findAll().get(0);


        NewsDto newsDto = NewsDto.builder()
                .id(news.getId().toString())
                .text("UpdatedNews")
                .userId(news.getUserId().toString())
                .updatedTime(news.getUpdatedTime())
                .createdTime(news.getCreatedTime())
                .build();

        given()
                .contentType(ContentType.JSON)
                .with()
                .body(newsDto)
                .header("userId", news.getUserId().toString())
                .when()
                .put(ApiCollection.UPDATE_NEWS)
                .then()
                .statusCode(200)
                .body("id", hasToString(news.getId().toString()))
                .body("text", hasToString("UpdatedNews"))
                .body("userId", equalTo(news.getUserId().toString()));

    }

    @Test
    @Order(4)
    public void whenDeleteNewsThenReturnOk() {

        News news = newsRepository.findAll().get(0);

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", news.getId())
                .with()
                .header("userId", news.getUserId())
                .when()
                .delete(ApiCollection.DELETE_NEWS)
                .then()
                .statusCode(200)
                .body(is("News успешно удалена"));
    }

    @Negative
    @Test
    @Order(5)
    public void whenCreateNewsWithEmptyFieldThenReturnError() {

        NewsDto newsDto = NewsDto.builder()
                .id(null)
                .text("")
                .categoryName("Category 1")
                .createdTime(null)
                .updatedTime(null)
                .build();

        given()
                .contentType(ContentType.JSON)
                .with()
                .body(newsDto)
                .header("userId", 1)
                .when()
                .post(ApiCollection.CREATE_NEWS)
                .then()
                .statusCode(200)
                .body("message", hasToString("[Field text must not be blank]"));
    }

    @Negative
    @Test
    @Order(6)
    public void whenUpdateNewsWithFalseUserIdThenReturnError() {

        News news = newsRepository.findAll().get(1);


        NewsDto newsDto = NewsDto.builder()
                .id(news.getId().toString())
                .text("UpdatedNews1")
                .userId(news.getUserId().toString())
                .updatedTime(news.getUpdatedTime())
                .createdTime(news.getCreatedTime())
                .build();

        given()
                .contentType(ContentType.JSON)
                .with()
                .body(newsDto)
                .header("userId", "000")
                .when()
                .put(ApiCollection.UPDATE_NEWS)
                .then()
                .statusCode(200)
                .body("message", hasToString("Текущий пользователь не имеет прав на изменение либо удаление объекта"));

    }

    @Negative
    @Test
    @Order(7)
    public void whenUpdateNewsWithEmptyFieldThenReturnError() {

        News news = newsRepository.findAll().get(1);


        NewsDto newsDto = NewsDto.builder()
                .id(news.getId().toString())
                .text("")
                .userId(news.getUserId().toString())
                .updatedTime(news.getUpdatedTime())
                .createdTime(news.getCreatedTime())
                .build();

        given()
                .contentType(ContentType.JSON)
                .with()
                .body(newsDto)
                .header("userId", news.getUserId())
                .when()
                .put(ApiCollection.UPDATE_NEWS)
                .then()
                .statusCode(200)
                .body("message", hasToString("[Field text must not be blank]"));

    }

    @Negative
    @Test
    @Order(8)
    public void whenDeleteNewsWithFailedIdThenReturnError() {
        List<News> listNews = newsRepository.findAll();

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", 0)
                .with()
                .header("userId", listNews.get(0).getUserId())
                .when()
                .delete(ApiCollection.DELETE_NEWS)
                .then()
                .statusCode(200)
                .body("message", hasToString("Объект не найден"));

    }

    @Negative
    @Test
    @Order(9)
    public void whenDeleteNewsWithFailedUserIdThenReturnError() {
        News news = newsRepository.findAll().get(1);

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", news.getId())
                .with()
                .header("userId", 4L)
                .when()
                .delete(ApiCollection.DELETE_NEWS)
                .then()
                .statusCode(200)
                .body("message", hasToString("Текущий пользователь не имеет прав на изменение либо удаление объекта"));

    }
}
