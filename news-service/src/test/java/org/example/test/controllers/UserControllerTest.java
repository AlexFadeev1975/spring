package org.example.test.controllers;

import io.restassured.http.ContentType;
import jakarta.validation.constraints.Negative;
import org.example.dto.UserDto;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.with;
import static org.hamcrest.Matchers.*;


public class UserControllerTest extends AbstractControllerTest {

    @Autowired
    UserRepository userRepository;

    @Test
    public void shouldCreateUser() {


        UserDto userRequest = UserDto.builder()
                .id(null)
                .firstName("Alex")
                .lastName("Fadeev")
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
    public void whenGetAllUsersReturnListUsers() {

        userRepository.save(new User(1L, "Alex", "Fadeev"));
        userRepository.save(new User(2L, "Ivan", "Petrov"));

        with()
                .contentType(ContentType.JSON)
                .when()
                .get(ApiCollection.GET_ALL_USERS)
                .then()
                .statusCode(200)
                .body(".", hasSize(2));

    }

    @Test
    public void whenUpdateUserReturnUser() {

        User user = userRepository.findAll().get(0);
        UserDto renewUser = UserDto.builder()
                .id(user.getId().toString())
                .firstName("Efim")
                .lastName("Shefrin")
                .build();

        given()
                .contentType(ContentType.JSON)
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
    public void whenDeleteUserReturnOk() {

        User user = userRepository.findAll().get(0);

        Long userId = user.getId();

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", userId)
                .when()
                .delete(ApiCollection.DELETE_USER)
                .then()
                .statusCode(200)
                .body(is("User успешно удален"));
    }

    @Negative
    @Test
    public void whenCreateUserWithEmptyFieldThenError() {

        UserDto userDto = UserDto.builder()
                .id(null)
                .lastName("")
                .firstName("Ivan")
                .build();

        given()
                .contentType(ContentType.JSON)
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
    public void whenUpdateWithEmptyUserIdThenError() {

        UserDto userDto = UserDto.builder()
                .id("")
                .firstName("Ivan")
                .lastName("Petrov")
                .build();

        given()
                .contentType(ContentType.JSON)
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
    public void whenDeleteUserIsNotDigitThenError() {

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", "s")
                .when()
                .delete(ApiCollection.DELETE_USER)
                .then()
                .statusCode(200)
                .body("message", hasToString("Неверный формат данных"));
    }

}
