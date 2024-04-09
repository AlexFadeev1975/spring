package org.example.mappers;

import org.example.dto.RoomDtoRequest;
import org.example.dto.RoomDtoResponse;
import org.example.model.Room;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoomMapper {

    Room  mapRoomDtoToRoom (RoomDtoRequest dto);

    RoomDtoResponse mapRoomToRoomDto (Room room);

}
