package org.example.mappers;

import lombok.RequiredArgsConstructor;
import org.example.dto.RoomDtoRequest;
import org.example.dto.RoomDtoResponse;
import org.example.model.Hotel;
import org.example.model.Room;
import org.example.repository.HotelRepository;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Mapper
@Component
@RequiredArgsConstructor
public class RoomMapperImpl implements RoomMapper{

    private final HotelRepository hotelRepository;

    @Override
    public Room mapRoomDtoToRoom(RoomDtoRequest dto) {

        Room room = new Room();
        room.setRoomName(dto.getRoomName());

        Hotel hotel = (dto.getHotelId() == null) ? null : hotelRepository.findById(dto.getHotelId()).orElseThrow(() -> new NoSuchElementException("Данного отеля не существует"));
        room.setHotel(hotel);
        room.setNumber(dto.getNumber());
        room.setPrice(dto.getPrice());
        room.setDescription(dto.getDescription());
        room.setPopulation(dto.getPopulation());

        return room;
    }

    @Override
    public RoomDtoResponse mapRoomToRoomDto(Room room) {

        RoomDtoResponse dto = new RoomDtoResponse();
        dto.setId(room.getId());
        dto.setRoomName(room.getRoomName());
        dto.setNumber(room.getNumber());
        dto.setDescription(room.getDescription());
        dto.setPopulation(room.getPopulation());
        dto.setPrice(room.getPrice());
        dto.setHotel(room.getHotel());
        dto.setBookedDates(room.getClosedDates());

        return dto;
    }
}
