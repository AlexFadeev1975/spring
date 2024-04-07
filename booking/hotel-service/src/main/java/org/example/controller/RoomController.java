package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.RoomDtoRequest;
import org.example.dto.RoomDtoResponse;
import org.example.services.RoomService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rooms/api/")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping("/v1/create")
    public RoomDtoResponse createRoom(@RequestBody @Valid RoomDtoRequest dto) {

        return roomService.createRoom(dto);
    }

    @GetMapping("/v2/room/{id}")
    public RoomDtoResponse findRoomById(@PathVariable("id") Long id) {

        return roomService.findRoomById(id);
    }

    @PutMapping("/v1/update")
    public RoomDtoResponse updateRoom(@RequestBody RoomDtoRequest dto) {

        return roomService.updateRoom(dto);
    }

    @DeleteMapping("/v1/delete/{id}")
    public ResponseEntity<?> deleteRoomById(@PathVariable("id") Long id) {

        return roomService.deleteRoomById(id);
    }
    @GetMapping("/v2/search")
    public List<RoomDtoResponse> searchRooms (@RequestBody RoomDtoRequest dto) {

        Pageable pageable = PageRequest.of(dto.getPage(), dto.getSize());

        return roomService.searchRoom(dto.getId(), dto.getRoomName(), dto.getNumber(), dto.getHotelId(), dto.getMaxPrice(), dto.getMinPrice(), dto.getArrivalDate(), dto.getLeavingDate(), pageable);

    }
}
