package org.example.test.controller;

import io.restassured.http.ContentType;
import jakarta.validation.constraints.Negative;
import org.example.dto.HotelDtoRequest;
import org.example.model.City;
import org.example.model.Hotel;
import org.example.model.User;
import org.example.model.enums.RoleType;
import org.example.repository.CityRepository;
import org.example.repository.HotelRepository;
import org.example.repository.UserRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.hasToString;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class HotelControllerTest extends AbstractBookingServiceTest {

    @Autowired
    HotelRepository hotelRepository;
    @Autowired
    CityRepository cityRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;


    @Test
    @Order(1)
    public void shouldCreateHotel() {

        User user = userRepository.save(new User(null, "User 1", passwordEncoder.encode("123"), "email@email.ru", RoleType.ROLE_ADMIN));

        City city = new City("Уржум", "Кировская обл");
        cityRepository.save(city);

        HotelDtoRequest dto = HotelDtoRequest.builder()
                .id(null)
                .hotelName("Hotel 1")
                .city("Уржум")
                .district("Кировская обл")
                .address("ул. Елки, 100")
                .header("Header 1")
                .build();

        given()
                .log()
                .all()
                .contentType(ContentType.JSON)
                .auth()
                .basic("User 1", "123")
                .with()
                .body(dto)
                .when()
                .post(ApiCollection.CREATE_HOTEL)
                .then()
                .statusCode(200)
                .body("id", is(notNullValue()))
                .body("hotelName", hasToString("Hotel 1"))
                .body("address", hasToString("ул. Елки, 100"))
                .body("header", hasToString("Header 1"));

    }

    @Test
    @Order(2)
    public void whenGetAllHotelsThenReturnListHotels() {

        Hotel hotel = Hotel.builder()
                .hotelName("Hotel 2")
                .city(new City("г. Москва", "Московская обл"))
                .address("ул. Мира, 10")
                .header("Header 2")
                .remotion(2f)
                .build();

        hotelRepository.save(hotel);

        given()
                .log()
                .all()
                .contentType(ContentType.JSON)
                .auth()
                .basic("User 1", "123")
                .when()
                .get(ApiCollection.GET_ALL_HOTELS)
                .then()
                .statusCode(200)
                .body(".", hasSize(2));
    }


    @Test
    @Order(3)
    public void whenUpdateHotelReturnHotel() {

        Hotel hotel = hotelRepository.findAll().get(0);


        HotelDtoRequest dto = HotelDtoRequest.builder()
                .id(hotel.getId())
                .hotelName("New Hotel")
                .header("New header")
                .remotion(5f)
                .build();

        given()
                .log()
                .all()
                .contentType(ContentType.JSON)
                .auth()
                .basic("User 1", "123")
                .with()
                .body(dto)
                .when()
                .put(ApiCollection.UPDATE_HOTEL)
                .then()
                .statusCode(200)
                .body("id", hasToString(hotel.getId().toString()))
                .body("hotelName", hasToString("New Hotel"))
                .body("header", hasToString("New header"));

    }

    @Test
    @Order(4)
    public void whenCreateRatingThenReturnOk() {
        Hotel hotel = hotelRepository.findAll().get(0);


        HotelDtoRequest dto = HotelDtoRequest.builder()
                .id(hotel.getId())
                .mark(5)
                .build();

        given()
                .log()
                .all()
                .contentType(ContentType.JSON)
                .auth()
                .basic("User 1", "123")
                .with()
                .body(dto)
                .when()
                .post(ApiCollection.RATE_HOTEL)
                .then()
                .statusCode(200)
                .body("id", hasToString(hotel.getId().toString()))
                .body("rating", is(5.0F));


    }

    @Test
    @Order(5)
    public void whenDeleteHotelThenReturnOk() {

        Hotel hotel = hotelRepository.findAll().get(0);

        given()
                .log()
                .all()
                .contentType(ContentType.JSON)
                .auth()
                .basic("User 1", "123")
                .pathParam("id", hotel.getId())
                .when()
                .delete(ApiCollection.DELETE_HOTEL)
                .then()
                .statusCode(200)
                .body(is("Отель успешно удален"));
    }

    @Negative
    @Test
    @Order(6)
    public void whenCreateHotelWithEmptyFieldThenReturnError() {

        HotelDtoRequest dto = HotelDtoRequest.builder()
                .id(null)
                .hotelName("Hotel 4")
                .city("Киров")
                .address("")
                .district("Кировская обл")
                .header("Header 3")
                .remotion(5f)
                .build();

        given()
                .log()
                .all()
                .contentType(ContentType.JSON)
                .auth()
                .basic("User 1", "123")
                .with()
                .body(dto)
                .when()
                .post(ApiCollection.CREATE_HOTEL)
                .then()
                .statusCode(200)
                .body("message", hasToString("[Поле Адрес не может быть пустым]"));
    }

    @Negative
    @Test
    @Order(7)
    public void whenUpdateHotelWithFalseIdThenReturnError() {

        Hotel hotel = hotelRepository.findAll().get(0);


        HotelDtoRequest dto = HotelDtoRequest.builder()
                .id(0L)
                .hotelName(hotel.getHotelName())
                .address(hotel.getAddress())
                .remotion(3f)
                .build();

        given()
                .log()
                .all()
                .contentType(ContentType.JSON)
                .auth()
                .basic("User 1", "123")
                .with()
                .body(dto)
                .when()
                .put(ApiCollection.UPDATE_HOTEL)
                .then()
                .statusCode(200)
                .body("message", hasToString("Отель не найден"));

    }


    @Negative
    @Test
    @Order(8)
    public void whenDeleteHotelWithFailedIdThenReturnError() {
        Hotel hotel = hotelRepository.findAll().get(0);

        given()
                .log()
                .all()
                .contentType(ContentType.JSON)
                .auth()
                .basic("User 1", "123")
                .pathParam("id", 0L)
                .when()
                .delete(ApiCollection.DELETE_HOTEL)
                .then()
                .statusCode(200)
                .body("message", hasToString("Отель не найден"));

    }

}
