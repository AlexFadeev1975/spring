package org.example.mappers;

import org.example.dto.HotelDtoRequest;
import org.example.dto.HotelDtoResponse;
import org.example.model.Hotel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = {CityMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface HotelMapper {

    @Mapping(target = "city", expression = "java(hotel.getCity().toString())")
    HotelDtoResponse mapHotelToDto(Hotel hotel);

    Hotel mapDtoToHotel(HotelDtoRequest request);
}
