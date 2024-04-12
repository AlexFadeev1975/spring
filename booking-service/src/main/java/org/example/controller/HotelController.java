package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.example.dto.HotelDtoRequest;
import org.example.dto.HotelDtoResponse;
import org.example.model.Hotel;
import org.example.services.HotelService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/hotels/api/")
@RequiredArgsConstructor
public class HotelController {

    private final HotelService hotelService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public HotelDtoResponse createHotel(@RequestBody @Valid HotelDtoRequest dto) {

        return hotelService.createHotel(dto);
    }

    @GetMapping("/list")
    @PreAuthorize("hasRole('USER')")
    public List<HotelDtoResponse> getAllHotels() {

        return hotelService.getAllHotels();
    }

    @GetMapping("/hotel/{id}")
    @PreAuthorize("hasRole('USER')")
    public HotelDtoResponse findHotelById(@PathVariable("id") Long id) {

        return hotelService.findHotelById(id);
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public HotelDtoResponse updateHotel(@RequestBody HotelDtoRequest dto) {

        return hotelService.updateHotel(dto);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteHotelById(@PathVariable("id") Long id) {

        hotelService.deleteHotelById(id);

        return ResponseEntity.ok("Отель успешно удален");

    }

    @PostMapping("/rating")
    @PreAuthorize("hasRole('USER')")
    public HotelDtoResponse changeHotelRating(@RequestBody HotelDtoRequest dto) throws ParseException {

        return hotelService.changeHotelRating(dto);
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('USER')")
    public List<HotelDtoResponse> searchHotels(@RequestBody HotelDtoRequest dto) {

        Pageable pageable = PageRequest.of(dto.getPage(), dto.getSize());

        return hotelService.searchHotel(dto.getId(), dto.getHotelName(), dto.getCity(), dto.getAddress(), dto.getHeader(), dto.getRemotion(), dto.getRating(), dto.getRatesCount(), pageable);


    }
}
