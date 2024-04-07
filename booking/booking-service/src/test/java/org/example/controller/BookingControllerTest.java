package org.example.controller;

import io.restassured.http.ContentType;
import org.example.dto.BookingDtoRequest;
import org.example.dto.HotelDtoRequest;
import org.example.model.City;
import org.example.model.Room;
import org.example.model.User;
import org.example.model.enums.RoleType;
import org.example.repository.BookingRepository;
import org.example.repository.RoomRepository;
import org.example.repository.UserRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.hasToString;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookingControllerTest extends AbstractBookingServiceTest {
    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    RoomRepository roomRepository;
@Autowired
    UserRepository userRepository;
@Autowired
 PasswordEncoder passwordEncoder;
    @Test
    @Order(1)
    public void shouldCreateBooking() {

        User user = userRepository.save(new User(2L, "Ivan", passwordEncoder.encode("123"),"my@email.ru", RoleType.ROLE_ADMIN));

        Room room = Room.builder()
                .roomName("Стандарт")
                .description("Norm")
                .number(222)
                .population(3)
                .price(2000f)
                .build();

        Room savedRoom = roomRepository.save(room);


        BookingDtoRequest dto = BookingDtoRequest.builder()
                .roomId(savedRoom.getId())
                .arrivalDate(LocalDate.parse("2024-03-01"))
                .leavingDate(LocalDate.parse("2024-03-05"))
                .build();

        given()
                .auth()
                .basic("Ivan", "123")
                .contentType(ContentType.JSON)
                .with()
                .body(dto)
                .when()
                .post(ApiCollection.CREATE_BOOKING)
                .then()
                .statusCode(200)
                .body("id", is(notNullValue()))
                .body("arrivalDate", hasToString("2024-03-01"))
                .body("leavingDate", hasToString("2024-03-05"));

    }



}
