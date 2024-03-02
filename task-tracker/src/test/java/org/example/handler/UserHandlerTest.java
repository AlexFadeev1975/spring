package org.example.handler;

import io.restassured.http.ContentType;
import org.example.model.User;
import org.example.model.dto.UserDto;
import org.example.model.enums.RoleType;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.hasToString;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class UserHandlerTest extends TaskTrackerTest {


    @Order(1)
    @Test
    public void whenCreateUserThenReturnNewUser() {

        String pass = passwordEncoder.encode("123");

        UserDto dto = new UserDto("1usr", "user 1", "email@us.ru", pass, "ROLE_MANAGER", Set.of(RoleType.ROLE_MANAGER));

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

        given()
                .contentType(ContentType.JSON)
                .auth()
                .basic("user 1", "123")
                .when()
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
                .auth()
                .basic("user 1", "123")
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
                .auth()
                .basic("user 1", "123")
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
                .auth()
                .basic("user 1", "123")
                .when()
                .delete(ApiCollection.DELETE_USER)
                .then()
                .statusCode(200);

    }

}
