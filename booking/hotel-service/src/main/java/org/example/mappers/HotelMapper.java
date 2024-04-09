package org.example.mappers;

import org.example.dto.HotelDtoRequest;
import org.example.dto.HotelDtoResponse;
import org.example.model.Hotel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HotelMapper {


    HotelDtoResponse mapHotelToDto (Hotel hotel);

    Hotel mapDtoToHotel (HotelDtoRequest request);
}
