package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.BookingDtoRequest;
import org.example.dto.BookingDtoResponse;
import org.example.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/booking/api/")

public class BookingController {
    @Autowired
    private BookingService bookingService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public BookingDtoResponse createBooking(@RequestBody BookingDtoRequest dto) {

        BookingDtoResponse dtos = bookingService.bookingRoom(dto);
        return dtos;

    }

    @GetMapping("/list/user")
    @PreAuthorize("hasRole('USER')")
    public List<BookingDtoResponse> getBookingByUser() {

        return bookingService.findAllByUser();
    }

    @GetMapping("/list/roomId/{id}")
    @PreAuthorize("hasRole('USER')")
    public List<BookingDtoResponse> getBookingByRoomId(@PathVariable("id") Long id) {

        return bookingService.findAllByRoom(id);
    }

    @GetMapping("/list")
    @PreAuthorize("hasRole('USER')")
    public List<BookingDtoResponse> getAllBooking() {
        List<BookingDtoResponse> list = bookingService.findAllBooking();
        return list;
    }

}
