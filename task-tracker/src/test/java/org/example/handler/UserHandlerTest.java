package org.example.handler;

import io.restassured.http.ContentType;
import org.example.model.User;
import org.example.model.dto.UserDto;
import org.example.repository.UserRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.hasToString;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class UserHandlerTest extends TaskTrackerTest {
    @Autowired
    private UserRepository userRepository;

    @Order(1)
    @Test
    public void whenCreateUserThenReturnNewUser() {

        UserDto dto = new UserDto("1usr", "user 1", "email@us.ru");

        given()
                .contentType(ContentType.JSON)
                .with()
                .body(dto)
                .when()
                .post(ApiCollection.CREATE_USER)
                .then()
                .statusCode(200)
                .body("username", hasToString("user 1"))
                .body("email", hasToString("email@us.ru"))
                .body("id", hasToString("1usr"));

    }

    @Test
    @Order(2)
    public void whenGetAllUsersThenFindSetUsers() {

        when()
                .get(ApiCollection.GET_USERS)
                .then()
                .body(".", hasSize(1));

    }

    @Test
    @Order(3)
    public void whenGetUserByIdThenFindUser() {

        User user = userRepository.findAll().blockFirst();

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", user.getId())
                .when()
                .get(ApiCollection.GET_USER)
                .then()
                .statusCode(200)
                .body("username", hasToString(user.getUsername()))
                .body("email", hasToString(user.getEmail()));

    }

    @Test
    @Order(4)
    public void whenUpdateUserThenReturnRenewalUser() {

        User user = userRepository.findAll().blockFirst();
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername("new user 1");
        dto.setEmail("newemail@emaii.ru");

        given()
                .contentType(ContentType.JSON)
                .with()
                .body(dto)
                .when()
                .put(ApiCollection.UPDATE_USER)
                .then()
                .statusCode(200)
                .body("username", hasToString("new user 1"))
                .body("email", hasToString("newemail@emaii.ru"));
    }

    @Test
    @Order(5)
    public void whenDeleteUserByIdReturnVoid() {

        User user = userRepository.findAll().blockFirst();

        given()
                .contentType(ContentType.JSON)
                .with()
                .pathParam("id", user.getId())
                .when()
                .delete(ApiCollection.DELETE_USER)
                .then()
                .statusCode(200);

    }

}
