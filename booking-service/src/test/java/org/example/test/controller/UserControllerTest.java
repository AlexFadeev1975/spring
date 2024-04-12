package org.example.test.controller;

import io.restassured.http.ContentType;
import org.example.dto.UserDto;
import org.example.model.User;
import org.example.model.enums.RoleType;
import org.example.repository.UserRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerTest extends AbstractBookingServiceTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Test
    @Order(1)
    public void whenCreateUserThenReturnOne() {


        UserDto dto = UserDto.builder()
                .userName("User 1")
                .password("123")
                .email("email@email.ru")
                .stringRole("ROLE_ADMIN")
                .build();

        given()
                .contentType(ContentType.JSON)
                .log()
                .all()
                .with()
                .body(dto)
                .post(ApiCollection.CREATE_USER)
                .then()
                .statusCode(200)
                .body("id", is(notNullValue()))
                .body("userName", hasToString("User 1"))
                .body("email", hasToString("email@email.ru"));
    }
    @Test
    @Order(2)
    public void whenGetAllUsersWhenReturnList () {

        userRepository.save(new User(2L, "User 2", passwordEncoder.encode("123"), "my@email.ru", RoleType.ROLE_ADMIN));

        given()
                .log()
                .all()
                .auth()
                .basic("User 1", "123")
                .contentType(ContentType.JSON)
                .get(ApiCollection.GET_ALL_USERS)
                .then()
                .statusCode(200)
                .body(".", hasSize(2));

   }
@Test
@Order(3)
   public void whenSearchUserByUserNameThenReturnOne () {

        User user = userRepository.findAll().get(0);

        given()
                .log()
                .all()
                .auth()
                .basic("User 1", "123")
                .contentType(ContentType.JSON)
                .pathParam("userName", user.getUserName())
                .get(ApiCollection.GET_USER_BY_USERNAME)
                .then()
                .statusCode(200)
                .body("id", hasToString(user.getId().toString()))
                .body("email", hasToString(user.getEmail()))
                .body("userName", hasToString(user.getUserName()));
   }
   @Test
   @Order(4)
   public void whenFindUserByIdThenReturnOne () {

       User user = userRepository.findAll().get(0);

       given()
               .log()
               .all()
               .auth()
               .basic("User 1", "123")
               .contentType(ContentType.JSON)
               .pathParam("id", user.getId().toString())
               .get(ApiCollection.GET_USER_BY_ID)
               .then()
               .statusCode(200)
               .body("id", hasToString(user.getId().toString()))
               .body("email", hasToString(user.getEmail()))
               .body("userName", hasToString(user.getUserName()));

   }
@Test
@Order(5)
   public void whenUpdateUserThenReturnOne () {

       User user = userRepository.findAll().get(0);

       UserDto dto = UserDto.builder()
               .id(user.getId())
               .userName("New User")
               .password("321")
               .stringRole("ROLE_USER")
               .email("new@email.ru")
               .build();

       given()
               .log()
               .all()
               .auth()
               .basic("User 1", "123")
               .contentType(ContentType.JSON)
               .with()
               .body(dto)
               .put(ApiCollection.UPDATE_USER)
               .then()
               .statusCode(200)
               .body("id", hasToString(user.getId().toString()))
               .body("userName", hasToString("New User"))
               .body("email", hasToString("new@email.ru"));


   }
@Test
@Order(6)
   public void  whenDeleteUserThenReturnOk () {

        User user = userRepository.findAll().get(0);

        given()
                .log()
                .all()
                .auth()
                .basic("User 2", "123")
                .contentType(ContentType.JSON)
                .pathParam("id", user.getId())
                .delete(ApiCollection.DELETE_USER_BY_ID)
                .then()
                .statusCode(200)
                .body(is("Пользователь успешно удален"));
   }

}
