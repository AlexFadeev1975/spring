package org.example.test.controllers;

import io.restassured.http.ContentType;
import jakarta.validation.constraints.Negative;
import org.example.dto.UserDto;
import org.example.model.User;
import org.example.model.enums.RoleType;
import org.example.repository.UserRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.with;
import static org.hamcrest.Matchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerTest extends AbstractControllerTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    @Test
    @Order(1)
    public void shouldCreateUser() {

        String pass = passwordEncoder.encode("123");

        UserDto userRequest = UserDto.builder()
                .id(null)
                .firstName("Alex")
                .lastName("Fadeev")
                .email("email@email.ru")
                .password(pass)
                .role("ROLE_ADMIN")
                .build();

        given()
                .contentType(ContentType.JSON)
                .with()
                .body(userRequest)
                .when()
                .post(ApiCollection.CREATE_USER)
                .then()
                .statusCode(200)
                .body("firstName", hasToString("Alex"))
                .body("lastName", hasToString("Fadeev"))
                .body("id", is(notNullValue()));


    }

    @Test
    @Order(2)
    public void whenGetAllUsersReturnListUsers() {


       userRepository.save(new User(2L, "Ivan", "Petrov","my@email.ru", "123", RoleType.ROLE_USER));

        given()
                .contentType(ContentType.JSON)
                .auth()
                .basic("email@email.ru","123" )
                .get(ApiCollection.GET_ALL_USERS)
                .then()
                 .statusCode(200)
                .body(".", hasSize(2));

    }

    @Test
    @Order(3)
    public void whenUpdateUserReturnUser() {

        User user = userRepository.findAll().get(0);
        UserDto renewUser = UserDto.builder()
                .id(user.getId().toString())
                .firstName("Efim")
                .lastName("Shefrin")
                .build();

        given()
                .contentType(ContentType.JSON)
                .auth()
                .basic("email@email.ru","123" )
                .with()
                .body(renewUser)
                .put(ApiCollection.UPDATE_USER)
                .then()
                .statusCode(200)
                .body("id", hasToString(user.getId().toString()))
                .body("firstName", hasToString("Efim"))
                .body("lastName", hasToString("Shefrin"));
    }

    @Test
    @Order(4)
    public void whenDeleteUserReturnOk() {

        User user = userRepository.findAll().get(0);

        Long userId = user.getId();

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", userId)
                .auth()
                .basic("email@email.ru","123" )
                .when()
                .delete(ApiCollection.DELETE_USER)
                .then()
                .statusCode(200)
                .body(is("User успешно удален"));
    }

    @Negative
    @Test
    @Order(5)
    public void whenCreateUserWithEmptyFieldThenError() {

        UserDto userDto = UserDto.builder()
                .id(null)
                .lastName("")
                .firstName("Ivan")
                .build();

        given()
                .contentType(ContentType.JSON)
                .auth()
                .basic("email@email.ru","123" )
                .with()
                .body(userDto)
                .when()
                .post(ApiCollection.CREATE_USER)
                .then()
                .assertThat()
                .statusCode(200)
                .body("message", hasToString("[Field lastName must not be blank]"));
    }

    @Negative
    @Test
    @Order(6)
    public void whenUpdateWithEmptyUserIdThenError() {

        UserDto userDto = UserDto.builder()
                .id("")
                .firstName("Ivan")
                .lastName("Petrov")
                .build();

        given()
                .contentType(ContentType.JSON)
                .auth()
                .basic("email@email.ru","123" )
                .with()
                .body(userDto)
                .when()
                .put(ApiCollection.UPDATE_USER)
                .then()
                .assertThat()
                .statusCode(200)
                .body("message", hasToString("[Field id must be digit]"));
    }

    @Negative
    @Test
    @Order(7)
    public void whenDeleteUserIsNotDigitThenError() {

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", "s")
                .auth()
                .basic("email@email.ru","123" )
                .when()
                .delete(ApiCollection.DELETE_USER)
                .then()
                .statusCode(200)
                .body("message", hasToString("Неверный формат данных"));
    }

}
