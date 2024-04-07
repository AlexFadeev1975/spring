package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.BookingDtoRequest;
import org.example.dto.BookingDtoResponse;
import org.example.services.BookingService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/booking/api/")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping("/v2/create")
    public BookingDtoResponse createBooking (@RequestBody BookingDtoRequest dto) {

        BookingDtoResponse dtos = bookingService.createBookingRoom(dto);
        return dtos;

    }
    @GetMapping("/v1/list/user")
    public List<BookingDtoResponse> getBookingByUser () {

        return bookingService.findAllByUser();
    }

    @GetMapping("/v1/list/roomId/{id}")
    public List<BookingDtoResponse> getBookingByRoomId (@PathVariable("id") Long id) {

        return bookingService.findAllByRoom(id);
    }

    @GetMapping("/v1/list")
    public List<BookingDtoResponse> getAllBooking () {

        return bookingService.finaAllBooking();
    }

}
