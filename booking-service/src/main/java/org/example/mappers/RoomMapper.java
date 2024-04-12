package org.example.mappers;

import org.example.dto.RoomDtoRequest;
import org.example.dto.RoomDtoResponse;
import org.example.model.Room;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDate;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoomMapper {

    Room mapRoomDtoToRoom(RoomDtoRequest dto);

    @Mapping(target = "hotelName", expression = "java(room.getHotel().getHotelName())")
    RoomDtoResponse mapRoomToRoomDto(Room room);


}
