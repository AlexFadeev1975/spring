package org.example.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.dto.RoomDtoRequest;
import org.example.dto.RoomDtoResponse;
import org.example.exceptions.SuchItemExistException;
import org.example.mappers.RoomMapper;
import org.example.model.Hotel;
import org.example.model.Room;
import org.example.repository.HotelRepository;
import org.example.repository.RoomRepository;
import org.example.search.filters.RoomFilterBuilder;
import org.example.search.utils.Filter;
import org.example.search.utils.SpecificationBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomMapper mapper;
    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final RoomFilterBuilder roomFilterBuilder;
    private final SpecificationBuilder<Room> specificationBuilder;

    @Transactional
    public RoomDtoResponse createRoom(RoomDtoRequest dto) {

        List<RoomDtoResponse> rooms = searchRoom(null, dto.getRoomName(), dto.getNumber(), dto.getHotelId(), null, null, null, null, null);

        Hotel hotel = hotelRepository.findById(dto.getHotelId()).orElseThrow(() -> new NoSuchElementException("Отеля с данным ID не существует"));
        dto.setHotel(hotel);

        if (rooms.isEmpty()) {

            return mapper.mapRoomToRoomDto(roomRepository.save(mapper.mapRoomDtoToRoom(dto)));

        } else throw new SuchItemExistException("Данная комната уже существует");

    }

    public RoomDtoResponse findRoomById(Long id) {

        Room room = roomRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Данная комната не существует"));
        return mapper.mapRoomToRoomDto(room);

    }

    public RoomDtoResponse updateRoom(RoomDtoRequest dto) {

        Room room = roomRepository.findById(dto.getId()).orElse(null);

        if (room != null) {
            Room updatedRoom = mapper.mapRoomDtoToRoom(dto);
            if (!updatedRoom.getRoomName().isEmpty()) {
                room.setRoomName(updatedRoom.getRoomName());
            }
            if (!updatedRoom.getDescription().isEmpty()) {
                room.setDescription(updatedRoom.getDescription());
            }
            if (updatedRoom.getNumber() != null) {
                room.setNumber(updatedRoom.getNumber());
            }
            if (updatedRoom.getPrice() != null) {
                room.setPrice(updatedRoom.getPrice());
            }
            if (updatedRoom.getPopulation() != null) {
                room.setPopulation(updatedRoom.getPopulation());
            }
            Room savedRoom = roomRepository.save(room);
            return mapper.mapRoomToRoomDto(savedRoom);
        } else throw new NoSuchElementException("Данной комнаты не существует");
    }

    public ResponseEntity<?> deleteRoomById(Long id) {

        Room room = roomRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Данная комната не существует"));

        roomRepository.delete(room);

        return ResponseEntity.ok("Комната успешно удалена");

    }

    public List<RoomDtoResponse> searchRoom(Long id, String roomName, Integer number, Long hotelId, Float maxPrice, Float minPrice, LocalDate arrivalDate, LocalDate leavingDate, Pageable pageable) {

        List<Filter> filters = roomFilterBuilder.createFilter(id, roomName, number, hotelId, maxPrice, minPrice, arrivalDate, leavingDate);

        Specification<Room> specification = specificationBuilder.getSpecificationFromFilters(filters);
        if (pageable != null) {
            return roomRepository.findAll(specification, pageable).getContent().stream().map(mapper::mapRoomToRoomDto).toList();
        } else return roomRepository.findAll(specification).stream().map(mapper::mapRoomToRoomDto).toList();
    }
}
