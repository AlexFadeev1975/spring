package org.example.test.controllers;

import io.restassured.http.ContentType;
import org.example.dto.CommentDto;
import org.example.model.Comment;
import org.example.model.News;
import org.example.repository.NewsRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.hasToString;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CommentControllerTest extends AbstractControllerTest {

    @Autowired
    NewsRepository newsRepository;

    @Test
    @Order(1)
    public void shouldCreateComment() {

        CommentDto commentDto = CommentDto.builder()
                .id(null)
                .text("Comment 1")
                .createdTime(null)
                .updatedTime(null)
                .news(null)
                .build();

        News news = newsRepository.save(new News("News 1", 1L));


        given()
                .contentType(ContentType.JSON)
                .pathParam("newsId", news.getId())
                .with()
                .header("userId", 1)
                .body(commentDto)
                .when()
                .post(ApiCollection.CREATE_COMMENT)
                .then()
                .statusCode(200)
                .body("id", is(notNullValue()))
                .body("text", hasToString("Comment 1"))
                .body("createdTime", matchesPattern("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}"))
                .body("userId", hasToString(String.valueOf(1)));
    }

    @Test
    @Order(2)
    public void whenGetAllCommentsThenReturnCommentsLIstOfNews() {


        News news = newsRepository.save(new News("News 2", 1L));
        Comment comment = Comment.builder()

                .text("Comment 1")
                .createdTime(LocalDateTime.now())
                .updatedTime(null)
                .news(null)
                .userId(1L)
                .build();

        Comment comment1 = Comment.builder()

                .text("Comment 2")
                .createdTime(LocalDateTime.now())
                .updatedTime(null)
                .news(null)
                .userId(1L)
                .build();

        news.setComments(List.of(comment, comment1));
        newsRepository.save(news);

        given()
                .contentType(ContentType.JSON)
                .pathParam("newsId", news.getId())
                .when()
                .get(ApiCollection.GET_ALL_COMMENTS)
                .then()
                .body(".", hasSize(2));
    }

    @Test
    @Order(3)
    public void whenUpdateCommentReturnComment() {

        News news = newsRepository.findAll().get(0);

        Comment comment = news.getComments().get(0);

        CommentDto commentDto = CommentDto.builder()
                .id(comment.getId().toString())
                .text("UpdatedComment")
                .userId(comment.getUserId().toString())
                .build();

        given()
                .contentType(ContentType.JSON)
                .with()
                .body(commentDto)
                .header("userId", comment.getUserId())
                .when()
                .put(ApiCollection.UPDATE_COMMENT)
                .then()
                .body("id", hasToString(comment.getId().toString()))
                .body("text", hasToString("UpdatedComment"));

    }

    @Test
    @Order(4)
    public void whenDeleteCommentThenReturnOk() {

        News news = newsRepository.findAll().get(1);

        Comment comment = news.getComments().get(0);

        given()
                .contentType(ContentType.JSON)
                .pathParam("commentId", comment.getId())
                .with()
                .header("userId", comment.getUserId())
                .when()
                .delete(ApiCollection.DELETE_COMMENT)
                .then()
                .body(is("Comment успешно удален"));

    }


}

