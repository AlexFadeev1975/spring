package org.example.test.controller;

import io.restassured.http.ContentType;
import jakarta.validation.constraints.Negative;
import org.example.dto.RoomDtoRequest;
import org.example.model.City;
import org.example.model.Hotel;
import org.example.model.Room;
import org.example.model.User;
import org.example.model.enums.RoleType;
import org.example.repository.HotelRepository;
import org.example.repository.RoomRepository;
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
public class RoomControllerTest extends AbstractBookingServiceTest {
    @Autowired
    RoomRepository roomRepository;
    @Autowired
    HotelRepository hotelRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;


    @Test
    @Order(1)
    public void shouldCreateRoom() {

        User user = userRepository.save(new User(null, "User 1", passwordEncoder.encode("123"), "email@email.ru", RoleType.ROLE_ADMIN));

        Hotel hotel = Hotel.builder()
                .hotelName("Hotel 2")
                .city(new City("г. Москва", "Московская обл"))
                .address("ул. Мира, 10")
                .header("Header 2")
                .remotion(2f)
                .build();

        Hotel savedHotel = hotelRepository.save(hotel);


        RoomDtoRequest dto = RoomDtoRequest.builder()
                .id(null)
                .roomName("Люкс")
                .description("Very good")
                .number(777)
                .population(2)
                .price(5000.0f)
                .hotelId(savedHotel.getId())
                .build();

        given()
                .log()
                .all()
                .auth()
                .basic("User 1", "123")
                .contentType(ContentType.JSON)
                .with()
                .body(dto)
                .when()
                .post(ApiCollection.CREATE_ROOM)
                .then()
                .statusCode(200)
                .body("id", is(notNullValue()))
                .body("roomName", hasToString("Люкс"))
                .body("description", hasToString("Very good"))
                .body("number", is(777))
                .body("population", is(2))
                .body("price", is(5000.0F));

    }

    @Test
    @Order(2)
    public void whenGetRoomByIdThenReturnRoom() {

        Hotel hotel = hotelRepository.findAll().get(0);

        Room room = Room.builder()
                .roomName("Стандарт")
                .description("Norm")
                .number(222)
                .population(3)
                .price(2000f)
                .hotel(hotel)
                .build();

        Room savedRoom = roomRepository.save(room);

        given()
                .log()
                .all()
                .auth()
                .basic("User 1", "123")
                .contentType(ContentType.JSON)
                .pathParam("id", savedRoom.getId())
                .when()
                .get(ApiCollection.GET_ROOM_BY_ID)
                .then()
                .statusCode(200)
                .body("roomName", hasToString("Стандарт"))
                .body("description", hasToString("Norm"))
                .body("number", is(222))
                .body("population", is(3))
                .body("price", is(2000F));
    }


    @Test
    @Order(3)
    public void whenUpdateRoomReturnRoom() {

        Room room = roomRepository.findAll().get(0);


        RoomDtoRequest dto = RoomDtoRequest.builder()
                .id(room.getId())
                .roomName("New name")
                .description("New description")
                .number(1)
                .price(2500f)
                .population(1)
                .build();

        given()
                .log()
                .all()
                .auth()
                .basic("User 1", "123")
                .contentType(ContentType.JSON)
                .with()
                .body(dto)
                .when()
                .put(ApiCollection.UPDATE_ROOM)
                .then()
                .statusCode(200)
                .body("id", is(notNullValue()))
                .body("roomName", hasToString("New name"))
                .body("description", hasToString("New description"))
                .body("number", is(1))
                .body("price", is(2500F))
                .body("population", is(1));

    }

    @Test
    @Order(4)
    public void whenDeleteRoomThenReturnOk() {

        Room room = roomRepository.findAll().get(0);

        given()
                .log()
                .all()
                .auth()
                .basic("User 1", "123")
                .contentType(ContentType.JSON)
                .pathParam("id", room.getId())
                .when()
                .delete(ApiCollection.DELETE_ROOM)
                .then()
                .statusCode(200)
                .body(is("Комната успешно удалена"));
    }

    @Negative
    @Test
    @Order(5)
    public void whenCreateRoomWithEmptyFieldThenReturnError() {

        RoomDtoRequest dto = RoomDtoRequest.builder()
                .id(null)
                .roomName("")
                .number(5)
                .price(1000f)
                .hotelId(1L)
                .description("Good")
                .build();

        given()
                .log()
                .all()
                .auth()
                .basic("User 1", "123")
                .contentType(ContentType.JSON)
                .with()
                .body(dto)
                .when()
                .post(ApiCollection.CREATE_ROOM)
                .then()
                .statusCode(200)
                .body("message", hasToString("[Поле Название комнаты не может быть пустым]"));
    }

    @Negative
    @Test
    @Order(6)
    public void whenUpdateHotelWithFalseIdThenReturnError() {


        RoomDtoRequest dto = RoomDtoRequest.builder()
                .id(0L)
                .roomName("Люкс")
                .number(2)
                .price(100f)
                .build();

        given()
                .log()
                .all()
                .auth()
                .basic("User 1", "123")
                .contentType(ContentType.JSON)
                .with()
                .body(dto)
                .when()
                .put(ApiCollection.UPDATE_ROOM)
                .then()
                .statusCode(200)
                .body("message", hasToString("Данной комнаты не существует"));

    }


    @Negative
    @Test
    @Order(7)
    public void whenDeleteRoomWithFailedIdThenReturnError() {


        given()
                .log()
                .all()
                .auth()
                .basic("User 1", "123")
                .contentType(ContentType.JSON)
                .pathParam("id", 0L)
                .when()
                .delete(ApiCollection.DELETE_ROOM)
                .then()
                .statusCode(200)
                .body("message", hasToString("Данная комната не существует"));

    }

}

